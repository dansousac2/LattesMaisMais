package com.ifpb.lattesmaismais.business;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;
import com.ifpb.lattesmaismais.presentation.exception.HashException;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

import io.jsonwebtoken.lang.Collections;

/**
 * Passa um currículo XML do Lattes importado pelo usuário para Classe
 * Curriculum
 * 
 * @version 2.0
 * @since 02/2023
 * @author Danilo
 *
 */
@Service
public class CurriculumXmlParseService extends DefaultHandler {

	@Value("${directory.file}")
	private String pathXmlCurriculum;
	@Autowired
	private HashService hashService;
	@Autowired
	private UserService userService;
	@Autowired
	private GenericsCurriculumService genCurriculumService;
	
	private Curriculum curriculum;
	private int ownerId;
	private int entryCount = 0;
	private HashMap<String, List<String>> hashEntry;

	@Autowired
	private CurriculumService curriculumService;

	private String group = "";
	private String filter;
	private String identifierEntry;
	private String baseToConcat;

	public CurriculumXmlParseService() {

	}

	/**
	 * Configura API SAX e deixa explícito o conjunto de caracteres (aqui UTF-8) a sr usado  
	 * @param pathArq
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void doParse(String pathArq) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// para capturar os names das tags
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		// início do parse
		// alternativa 1 - o "this" indica que a própria classe ficará responsável por
		// gerenciar os eventos SAX.
//		saxParser.parse(pathArq, this);

		// aLternativa 2 - modificando o decodificador para reconhecer o padrão do XML
		// (ISO-8859-1),
		// onde não usamos o .parse do saxParse
		XMLReader xmlReader = saxParser.getXMLReader();
		// passando esta classe como hadler para o leitor XML
		xmlReader.setContentHandler(this);
		// caminho o arquivo
		InputSource source = new InputSource(pathArq);
		// tipo de charset
		source.setEncoding(StandardCharsets.UTF_8.displayName());
		xmlReader.parse(source);
	}

	/**
	 * Realiza ações apenas no início do processo de leitura do XML
	 */
	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("- - - Início de parse de documento - - -\n");
		hashEntry = new HashMap<>();
	}

	/**
	 * Realiza ações apnas durante o fim do processo de leitura do XML
	 */
	public void endDocument() throws SAXException {
		super.endDocument();
		// preparando currículo
		try {
			buildCurriculum();
			entryCount = 0;
			filter = null;
		} catch (ObjectNotFoundException e) {
			throw new SAXException("Erro API SAX - id de usuário não encontrado: " + e.getMessage());
		}
		System.out.println("\n- - - Fim de parse de documento! - - -");
	}

	/**
	 * Método que é sempre disparado quando o processador SAX identifica a abertura de uma tag.
	 * Ele possibilita a captura do nome da tag e dos nomes e valores de todos os atributos associados a esta tag,
	 * caso eles existam.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		switch (localName) {
		case "DADOS-GERAIS":
			System.out.println("Currículo pertencente a: " + attributes.getValue(0));
			break;

		case "FORMACAO-ACADEMICA-TITULACAO":
			createAndSetGroup("Formação Acadêmica");
			break;
		case "GRADUACAO":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "ESPECIALIZACAO":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "MESTRADO":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "ENSINO-MEDIO-SEGUNDO-GRAU":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;

		case "ATUACAO-PROFISSIONAL":
			createAndSetGroup("Atuação Profissional");
			filter = "NOME-INSTITUICAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "VINCULOS":
			filter = "ANO-INICIO ANO-FIM OUTRO-ENQUADRAMENTO-FUNCIONAL-INFORMADO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "PROJETO-DE-PESQUISA":
			createAndSetGroup("Participação em projetos");
			filter = "ANO-INICIO ANO-FIM NOME-DO-PROJETO SITUACAO NATUREZA";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DIRECAO-E-ADMINISTRACAO":
			createAndSetGroup("Atividade de direção e adm.");
			filter = "ANO-INICIO ANO-FIM NOME-ORGAO CARGO-OU-FUNCAO";
			extractAttAndConcatTags(attributes, false);
			break;

		// para trabalhos em eventos, as próximas 3 tags formarão 1 entrada no currículo
		case "TRABALHOS-EM-EVENTOS":
			createAndSetGroup("Trabalho em eventos");
			break;
		case "DADOS-BASICOS-DO-TRABALHO":
			filter = "TITULO-DO-TRABALHO ANO-DO-TRABALHO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DO-TRABALHO":
			filter = "NOME-DO-EVENTO CIDADE-DO-EVENTO ANO-DE-REALIZACAO TITULO-DOS-ANAIS-OU-PROCEEDINGS";
			extractAttAndConcatTags(attributes, false);
			break;

		// 3 tags -> 1 entrada
		case "CAPITULOS-DE-LIVROS-PUBLICADOS":
			createAndSetGroup("Capítulos de livros publicados");
			break;
		case "DADOS-BASICOS-DO-CAPITULO":
			filter = "TITULO-DO-CAPITULO-DO-LIVRO ANO PAIS-DE-PUBLICACAO HOME-PAGE-DO-TRABALHO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DO-CAPITULO":
			filter = "TITULO-DO-LIVRO PAGINA-INICIAL PAGINA-FINAL NOME-DA-EDITORA";
			extractAttAndConcatTags(attributes, false);
			break;

		// para produção técnica
		case "PRODUCAO-TECNICA":
			createAndSetGroup("Produção Técnica");
			break;
		case "SOFTWARE":
			baseToConcat = "> software";
			break;
		case "DADOS-BASICOS-DO-SOFTWARE":
			filter = "NATUREZA TITULO-DO-SOFTWARE ANO HOME-PAGE-DO-TRABALHO";
			extractAttAndConcatTags(attributes, false);
			break;
		// grupo produção técnica
		case "PROCESSOS-OU-TECNICAS":
			baseToConcat = "> processos ou técnicas";
			break;
		case "DADOS-BASICOS-DO-PROCESSOS-OU-TECNICAS":
			filter = "NATUREZA TITULO-DO-PROCESSO ANO PAIS HOME-PAGE-DO-TRABALHO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DO-PROCESSOS-OU-TECNICAS":
			filter = "CIDADE-DO-PROCESSO";
			extractAttAndConcatTags(attributes, false);
			break;
		// grupo produção técnica
		case "TRABALHO-TECNICO":
			baseToConcat = "> trabalho técnico";
			break;
		case "DADOS-BASICOS-DO-TRABALHO-TECNICO":
			filter = "NATUREZA TITULO-DO-TRABALHO-TECNICO ANO PAIS HOME-PAGE-DO-TRABALHO";
			extractAttAndConcatTags(attributes, false);
			break;
		// grupo produção técnica
		case "APRESENTACAO-DE-TRABALHO":
			baseToConcat = "> apresentação de trabalho";
			break;
		case "DADOS-BASICOS-DA-APRESENTACAO-DE-TRABALHO":
			filter = "NATUREZA TITULO ANO PAIS";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-APRESENTACAO-DE-TRABALHO":
			filter = "NOME-DO-EVENTO INSTITUICAO-PROMOTORA CIDADE-DA-APRESENTACAO";
			extractAttAndConcatTags(attributes, false);
			break;
		// grupo produção técnica
		case "DADOS-BASICOS-DA-ORGANIZACAO-DE-EVENTO":
			filter = "TIPO NATUREZA TITULO ANO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-ORGANIZACAO-DE-EVENTO":
			filter = "INSTITUICAO-PROMOTORA CIDADE";
			extractAttAndConcatTags(attributes, false);
			break;
		// grupo produção técnica
		case "DADOS-BASICOS-DE-OUTRA-PRODUCAO-TECNICA":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE-DO-TRABALHO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DE-OUTRA-PRODUCAO-TECNICA":
			filter = "INSTITUICAO-PROMOTORA CIDADE";
			extractAttAndConcatTags(attributes, false);
			break;
		
		// NOVO GRUPO = outra produção
		case "OUTRA-PRODUCAO":
			createAndSetGroup("Outras Produções");
			break;
		case "ORIENTACOES-CONCLUIDAS":
			baseToConcat = "> orientações concluídas";
			break;
		case "DADOS-BASICOS-DE-OUTRAS-ORIENTACOES-CONCLUIDAS":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DE-OUTRAS-ORIENTACOES-CONCLUIDAS":
			filter = "NOME-DO-ORIENTADO NOME-DA-INSTITUICAO NOME-DO-CURSO";
			extractAttAndConcatTags(attributes, false);
			break;
			
		// formação complementar / 3 tags abaixo
		case "FORMACAO-COMPLEMENTAR":
			createAndSetGroup("Formação Complementar");
			break;
		case "FORMACAO-COMPLEMENTAR-DE-EXTENSAO-UNIVERSITARIA":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "FORMACAO-COMPLEMENTAR-CURSO-DE-CURTA-DURACAO":
			filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
			extractAttAndConcatTags(attributes, false);
			break;
			
		// NOVO GRUPO Participação em bancas acadêmicas
		case "PARTICIPACAO-EM-BANCA-TRABALHOS-CONCLUSAO":
			createAndSetGroup("Participação em bancas acadêmicas");
			break;
		case "PARTICIPACAO-EM-BANCA-DE-GRADUACAO":
			baseToConcat = "> graduação";
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-BANCA-DE-GRADUACAO":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-BANCA-DE-GRADUACAO":
			filter = "NOME-DO-CANDIDATO NOME-INSTITUICAO NOME-CURSO";
			extractAttAndConcatTags(attributes, false);
			break;
		
		// NOVO GRUPO Participação em bancas julgadoras
		case "PARTICIPACAO-EM-BANCA-JULGADORA":
			createAndSetGroup("Participação em bancas julgadoras");
			break;
		case "DADOS-BASICOS-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-BANCA-JULGADORA-PARA-CONCURSO-PUBLICO":
			filter = "NOME-INSTITUICAO";
			extractAttAndConcatTags(attributes, false);
			break;

		// GRUPO Participação em eventos e congressos
		case "PARTICIPACAO-EM-EVENTOS-CONGRESSOS":
			createAndSetGroup("Participação em Eventos e Congressos");
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-CONGRESSO":
			filter = "NATUREZA ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-CONGRESSO":
			filter = "NOME-DO-EVENTO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-SEMINARIO":
			filter = "NATUREZA ANO HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SEMINARIO":
			filter = "NOME-DO-EVENTO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-SIMPOSIO":
			filter = "NATUREZA ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SIMPOSIO":
			filter = "NOME-DO-EVENTO NOME-INSTITUICAO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-ENCONTRO":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-ENCONTRO":
			filter = "NOME-DO-EVENTO NOME-INSTITUICAO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-OFICINA":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-OFICINA":
			filter = "NOME-DO-EVENTO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-EXPOSICAO":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-EXPOSICAO":
			filter = "NOME-DO-EVENTO NOME-INSTITUICAO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;
		case "DADOS-BASICOS-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			filter = "NATUREZA TITULO ANO PAIS HOME-PAGE-DO-TRABALHO FORMA-PARTICIPACAO";
			extractAttAndConcatTags(attributes, true);
			break;
		case "DETALHAMENTO-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			filter = "NOME-DO-EVENTO NOME-INSTITUICAO CIDADE-DO-EVENTO";
			extractAttAndConcatTags(attributes, false);
			break;

		}
	}

	/**
	 * Método disparado quando o processador SAX identifica o fechamento de uma tag.
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);

		switch (localName) {

		case "FORMACAO-ACADEMICA-TITULACAO":
			baseToConcat = null;
			break;

		case "ATUACAO-PROFISSIONAL":
			baseToConcat = null;
			group = null;
			break;

		case "TRABALHOS-EM-EVENTOS":
			group = null;
			break;
		case "DETALHAMENTO-DO-TRABALHO":
			baseToConcat = null;
			break;

		case "CAPITULOS-DE-LIVROS-PUBLICADOS":
			group = null;
			break;
		case "DETALHAMENTO-DO-CAPITULO":
			baseToConcat = null;
			break;

		case "PRODUCAO-TECNICA":
			group = null;
			break;
		case "SOFTWARE":
			baseToConcat = null;
			break;
		case "PROCESSOS-OU-TECNICAS":
			baseToConcat = null;
			break;
		case "TRABALHO-TECNICO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-APRESENTACAO-DE-TRABALHO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-ORGANIZACAO-DE-EVENTO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DE-OUTRA-PRODUCAO-TECNICA":
			baseToConcat = null;
			break;
			
		case "OUTRA-PRODUCAO":
			group = null;
			break;
		case "ORIENTACOES-CONCLUIDAS":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DE-OUTRAS-ORIENTACOES-CONCLUIDAS":
			baseToConcat = "> orientações concluídas";
			break;
		case "FORMACAO-COMPLEMENTAR":
			group = null;
			break;
			
		case "PARTICIPACAO-EM-BANCA-TRABALHOS-CONCLUSAO":
			group = null;
			break;
		case "PARTICIPACAO-EM-BANCA-DE-GRADUACAO":
			baseToConcat = null;
			break;
			
		case "PARTICIPACAO-EM-BANCA-JULGADORA":
			group = null;
			break;
		case "BANCA-JULGADORA-PARA-CONCURSO-PUBLICO":
			baseToConcat = null;
			break;

		case "PARTICIPACAO-EM-EVENTOS-CONGRESSOS":
			group = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-CONGRESSO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SEMINARIO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SIMPOSIO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-ENCONTRO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-OFICINA":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-EXPOSICAO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			baseToConcat = null;
			break;
		}
	}
	
	/**
	 * Recebe uma String identificando o nome do grupo a qual a tag fará parte
	 * @param group
	 */
	private void createAndSetGroup(String group) {
		if (!hashEntry.containsKey(group)) {
			hashEntry.put(group, new ArrayList<>());
		}
		this.group = group;
	}

	private void extractAttAndConcatTags(Attributes attributes, boolean concat) {
		extractAttributes(attributes);
		concatTags(attributes, concat);
	}

	/*
	 * cria o identificador da entrada, podendo este ser parcial ou completo,
	 * referendo-se a lista de filtros/palavras-chave que foi dada
	 */
	private void extractAttributes(Attributes attributes) {
		String identifierEntry = "";
		if (baseToConcat != null) {
			identifierEntry = baseToConcat;
		}
		// com a String contendo os termos, criamos um array para verificação da palavra
		// por inteiro
		List<String> arrayList = Collections.arrayToList(filter.split(" "));

		// cada atributo da tag é analisada com base no filtro em array
		for (int i = 0; attributes.getLocalName(i) != null; i++) {
			String prop = attributes.getLocalName(i);

			if (arrayList.contains(prop)) {
				identifierEntry += "> " + (attributes.getValue(i).isBlank() ? "--" : attributes.getValue(i));
			}
		}

		this.identifierEntry = identifierEntry;
	}

	/*
	 * verifica se a tag da vez, que gerou o identificador de entrada
	 * (identifierEntry), deve ser concatenada com a próxima tag ou se deve ser
	 * adicionada ao hashmap
	 */
	private void concatTags(Attributes attributes, boolean concat) {
		if (concat) {
			/*
			 * caso positivo para concatenação / ainda não salva nos grupos do hashmap deixa
			 * o valor do identificador de entrada para a variável de concatenação
			 */
			baseToConcat = identifierEntry;
		} else {
			// caso negativo para concatenação / o nome da entrada está completo / salva no
			// hashmap
			hashEntry.get(group).add(identifierEntry);
			entryCount++;
			System.out.println(String.format("Entrada %d adicionada ao grupo %s", entryCount, group)); // teste(?)
			System.out.println("tamanho no nome da entrada: " + identifierEntry.length() + "\n" + identifierEntry + "\n\n"); // teste(?)
		}
	}

	private void buildCurriculum() throws ObjectNotFoundException {
		curriculum = new Curriculum();
		curriculum.setEntryCount(entryCount);
		curriculum.setEntries(hashStringsToListEntries(hashEntry));
		curriculum.setOwner(userService.findById(ownerId));
		curriculum.setStatus(CurriculumStatus.UNCHECKED);
		curriculum.setDescription("Primeira versão criada");
		curriculum.setVersion(genCurriculumService.createVersionName());
		curriculum.setLastModification(LocalDateTime.now());
	}

	private List<Entry> hashStringsToListEntries(HashMap<String, List<String>> hashmap) {
		List<Entry> listEntry = new ArrayList<>();

		for (Map.Entry<String, List<String>> pair : hashmap.entrySet()) {

			for (String s : pair.getValue()) {
				Entry entry = new Entry();
				entry.setGroup(pair.getKey());
				entry.setName(s);
				entry.setStatus(ReceiptStatus.WITHOUT_RECEIPT);

				listEntry.add(entry);
			}
		}

		return listEntry;
	}

	/**
	 * Realiza o mapeamento do currículo Lattes em XML para objeto,
	 * salvando uma versão (Curriculum) no DB com comentário padrão 
	 * @param userId
	 * @return The ID of curriculum saved in DB
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws HashException
	 */
	public Integer xmlToCurriculum(String userId)
			throws ParserConfigurationException, SAXException, IOException, HashException {

		ownerId = Integer.parseInt(userId);
		String hashIdUser = hashService.hashingSHA256(userId);

		doParse(pathXmlCurriculum + String.format("\\%s\\curriculum.xml", hashIdUser));

		Curriculum curriculumSaved = curriculumService.save(curriculum);
		
		return curriculumSaved.getId();
	}

}
