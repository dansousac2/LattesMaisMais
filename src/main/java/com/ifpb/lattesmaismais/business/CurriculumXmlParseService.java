package com.ifpb.lattesmaismais.business;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import com.ifpb.lattesmaismais.model.enums.EntryStatus;
import com.ifpb.lattesmaismais.presentation.exception.HashException;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

import io.jsonwebtoken.lang.Collections;
/**
 * Passa um currículo XML do Lattes importado pelo usuário para Classe Curriculum 
 * @version 1.0
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

	// Realiza conversão de XML para objeto
	public void doParse(String pathArq) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// para capturar os names das tags
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		// início do parse
		// alternativa 1 - o "this" indica que a própria classe ficará responsável por gerenciar os eventos SAX.
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

	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("- - - Início de parse de documento - - -\n");
		hashEntry = new HashMap<>();
	}

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
	 * evento startElement do SAX. disparado quando o processador SAX identifica a
	 * abertura de uma tag. Ele possibilita a captura do nome da tag e dos nomes e
	 * valores de todos os atributos associados a esta tag, caso eles existam.
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
			academicEducation(attributes);
			break;

		case "ESPECIALIZACAO":
			academicEducation(attributes);
			break;
			
		case "ENSINO-MEDIO-SEGUNDO-GRAU":
			academicEducation(attributes);
			break;
			
		case "ATUACAO-PROFISSIONAL":
			professionalPerformance(attributes);
			break;

		case "VINCULOS":
			professionalPerformance(attributes);
			break;

		case "PROJETO-DE-PESQUISA":
			withinProjects(attributes);
			break;

		// para trabalhos em eventos, as próximas 3 tags formarão 1 entrada no currículo 
		case "TRABALHOS-EM-EVENTOS":
			createAndSetGroup("Trabalho em eventos");
			break;
		case "DADOS-BASICOS-DO-TRABALHO":
			workInEvents(attributes);
			break;
		case "DETALHAMENTO-DO-TRABALHO":
			workInEvents(attributes);
			break;
		
		// para produção técnica / 3 tags abaixo = 1 entrada
		case "PRODUCAO-TECNICA":
			createAndSetGroup("Produção Técnica");
			break;
		case "DADOS-BASICOS-DA-ORGANIZACAO-DE-EVENTO":
			tecnicalProduction(attributes);
			break;
		case "DETALHAMENTO-DA-ORGANIZACAO-DE-EVENTO":
			tecnicalProduction(attributes);
			break;
		
		// formação complementar / 3 tags abaixo 
		case "FORMACAO-COMPLEMENTAR":
			createAndSetGroup("Formação Complementar");
			break;
		case "FORMACAO-COMPLEMENTAR-DE-EXTENSAO-UNIVERSITARIA":
			complemFormation(attributes);
			break;
		case "FORMACAO-COMPLEMENTAR-CURSO-DE-CURTA-DURACAO":
			complemFormation(attributes);
			break;
		
		// participação em eventos e congressos / multiplas entradas formadas a partir deste grupo 
		case "PARTICIPACAO-EM-EVENTOS-CONGRESSOS":
			createAndSetGroup("Participação em Eventos e Congressos");
			break;

		// participação em seminário / 2 abaixo
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-SEMINARIO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SEMINARIO":
			participationEventsConferences(attributes);
			break;
			
		// participação em oficinas / 2 abaixo
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-OFICINA":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-OFICINA":
			participationEventsConferences(attributes);
			break;
		
		// participação em encontro / 2 abaixo
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-ENCONTRO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-ENCONTRO":
			participationEventsConferences(attributes);
			break;

		// participação em exposição / 2 abaixo
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-EXPOSICAO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-EXPOSICAO":
			participationEventsConferences(attributes);
			break;
		
		
		// participação em outras participações / 2 abaixo
		case "DADOS-BASICOS-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			participationEventsConferences(attributes);
			break;
			
		case "+1":
			break;
		case "+2":
			break;
		case "+3":
			break;
		default:
			break;
		}
	}

	/**
	 * evento endElement do SAX. Disparado quando o processador SAX identifica o
	 * fechamento de uma tag (ex: </nome>)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		
		switch (localName) {
		
		case "FORMACAO-ACADEMICA-TITULACAO":
			baseToConcat = null;
			break;
			
		case "ATUACAO-PROFISSIONAL":
			baseToConcat = null;
			break;
		
		case "TRABALHOS-EM-EVENTOS":
			group = null;
			break;
		case "DETALHAMENTO-DO-TRABALHO":
			baseToConcat = null;
			break;

		case "PRODUCAO-TECNICA":
			group = null;
			break;
		case "DETALHAMENTO-DA-ORGANIZACAO-DE-EVENTO":
			baseToConcat = null;
			break;
			
		case "FORMACAO-COMPLEMENTAR":
			group = null;
			break;
			
		case "PARTICIPACAO-EM-EVENTOS-CONGRESSOS":
			group = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SEMINARIO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-OFICINA":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-ENCONTRO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-EXPOSICAO":
			baseToConcat = null;
			break;
		case "DETALHAMENTO-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			baseToConcat = null;
			break;
		
		default:
			break;
		}
	}

	/**
	 * evento characters do SAX. É onde podemos recuperar as informações texto
	 * contidas no documento XML (textos contidos entre tags). EX: <tag> texto
	 * recuperado </tag>
	 */

	private void academicEducation(Attributes attributes) {
		filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
		extractAttAndConcatTags(attributes, 0, "no concat");
	}

	private void professionalPerformance(Attributes attributes) {
		createAndSetGroup("Atuação Profissional");
		filter = "NOME-INSTITUICAO ANO-INICIO ANO-FIM OUTRO-ENQUADRAMENTO-FUNCIONAL-INFORMADO";
		extractAttAndConcatTags(attributes, 1, "NOME-INSTITUICAO");
	}
	
	private void withinProjects(Attributes attributes) {
		createAndSetGroup("Participação em projetos");
		filter = "ANO-INICIO ANO-FIM NOME-DO-PROJETO SITUACAO NATUREZA";
		extractAttAndConcatTags(attributes, 1, "NOME-INSTITUICAO");
	}
	
	private void workInEvents(Attributes attributes) {
		filter = "TITULO-DO-TRABALHO ANO-DO-TRABALHO NOME-DO-EVENTO CIDADE-DO-EVENTO ANO-DE-REALIZACAO TITULO-DOS-ANAIS-OU-PROCEEDINGS";
		extractAttAndConcatTags(attributes, 0, "NATUREZA");
	}
	
	private void tecnicalProduction(Attributes attributes) {
		filter = "TIPO TITULO ANO INSTITUICAO-PROMOTORA CIDADE";
		extractAttAndConcatTags(attributes, 0, "TIPO");
	}

	private void complemFormation(Attributes attributes) {
		filter = "NOME-INSTITUICAO NOME-CURSO STATUS-DO-CURSO ANO-DE-INICIO ANO-DE-CONCLUSAO";
		extractAttAndConcatTags(attributes, 0, "no concat");
	}

	private void participationEventsConferences(Attributes attributes) {
		filter = "NATUREZA ANO FORMA-PARTICIPACAO NOME-DO-EVENTO CIDADE-DO-EVENTO";
		extractAttAndConcatTags(attributes, 0, "NATUREZA");
	}

	private void extractAttAndConcatTags(Attributes attributes, int indexLocalNameAttribute, String localNameAttribute) {
		extractAttributes(attributes);
		concatTags(attributes, indexLocalNameAttribute, localNameAttribute);
	}

	/*
	 * cria o identificador da entrada, podendo este ser parcial ou completo, referendo-se a lista de filtros/palavras-chave
	 * que foi dada
	 */
	private void extractAttributes(Attributes attributes) {
		String identifierEntry = "";
		if (baseToConcat != null) {
			identifierEntry = baseToConcat;
		}
		// com a String contendo os termos, criamos um array para verificação da palavra por inteiro
		List<String> arrayList = Collections.arrayToList(filter.split(" "));
		
		// cada atributo da tag é analisada com base no filtro em array
		for (int i = 0; attributes.getLocalName(i) != null; i++) {
			String prop = attributes.getLocalName(i);
			
			if (arrayList.contains(prop)) {
				identifierEntry += "/" + (attributes.getValue(i).isBlank() ? "--" : attributes.getValue(i));
			}
		}

		this.identifierEntry = identifierEntry;
	}

	/*
	 * verifica se a tag da vez, que gerou o identificador de entrada (identifierEntry), deve ser concatenada
	 * com a próxima tag ou se deve ser adicionada ao hashmap
	 */
	private void concatTags(Attributes attributes, int index, String tagName) {
		if (attributes.getLocalName(index).equals(tagName)) {
			/* caso positivo para concatenação / ainda não salva nos grupos do hashmap
			 * deixa o valor do identificador de entrada para a variável de concatenação
			 */
			baseToConcat = identifierEntry;
		} else {
			// caso negativo para concatenação / o nome da entrada está completo / salva no hashmap
			hashEntry.get(group).add(identifierEntry);
			entryCount++;
			System.out.println(String.format("Entrada %d adicionada ao grupo %s", entryCount, group)); // teste(?)
			System.out.println("tamanho no nome da entrada: " + identifierEntry.length() + "\n" + identifierEntry + "\n\n"); // teste(?)
		}
	}

	private void createAndSetGroup(String group) {
		if (!hashEntry.containsKey(group)) {
			hashEntry.put(group, new ArrayList<>());
		}
		this.group = group;
	}

	private void buildCurriculum() throws ObjectNotFoundException {
		curriculum = new Curriculum();
		curriculum.setEntryCount(entryCount);
		// setar entradas do currículo
		curriculum.setEntries(hashStringsToListEntries(hashEntry));
		curriculum.setOwner(userService.findById(ownerId));
		curriculum.setStatus(CurriculumStatus.UNCHECKED);
	}

	private List<Entry> hashStringsToListEntries(HashMap<String, List<String>> hashmap) {
		List<Entry> listEntry = new ArrayList<>();

		for (Map.Entry<String, List<String>> pair : hashmap.entrySet()) {

			for (String s : pair.getValue()) {
				Entry entry = new Entry();
				entry.setGroup(pair.getKey());
				entry.setName(s);
				entry.setStatus(EntryStatus.WITHOUT_RECEIPT);
				
				listEntry.add(entry);
			}
		}

		return listEntry;
	}

	public Curriculum xmlToCurriculum(String userId) throws ParserConfigurationException, SAXException, IOException, HashException {
		
		ownerId = Integer.parseInt(userId);
		String hashIdUser = hashService.hashingSHA256(userId);
		
		doParse(pathXmlCurriculum + String.format("\\%s\\curriculum.xml", hashIdUser));
		
		return curriculumService.save(curriculum);
	}

}
