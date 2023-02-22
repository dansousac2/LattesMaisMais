package com.ifpb.lattesmaismais.business;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;

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

	@Autowired
	private Curriculum curriculum;
	private String owner;
	private int entryCount = 0;
	private HashMap<String, List<String>> hashEntry;
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
		hashEntry = new HashMap<>(); // TODO retirar para testes com API
	}

	public void endDocument() throws SAXException {
		super.endDocument();
		// preparando currículo
		buildCurriculum();
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
			owner = attributes.getValue(0);
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

		case "TRABALHOS-EM-EVENTOS":
			createAndSetGroup("Trabalho em eventos");
			break;
		
		case "DADOS-BASICOS-DO-TRABALHO":
			workInEvents(attributes);
			break;
			
		case "DETALHAMENTO-DO-TRABALHO":
			workInEvents(attributes);
			break;
			
		case "PRODUCAO-TECNICA":
			createAndSetGroup("Produção Técnica");
			break;
			
		case "DADOS-BASICOS-DA-ORGANIZACAO-DE-EVENTO":
			tecnicalProduction(attributes);
			break;
			
		case "DETALHAMENTO-DA-ORGANIZACAO-DE-EVENTO":
			tecnicalProduction(attributes);
			break;
			
		case "FORMACAO-COMPLEMENTAR":
			createAndSetGroup("Formação Complementar");
			break;
			
		case "FORMACAO-COMPLEMENTAR-DE-EXTENSAO-UNIVERSITARIA":
			complemFormation(attributes);
			break;
			
		case "FORMACAO-COMPLEMENTAR-CURSO-DE-CURTA-DURACAO":
			complemFormation(attributes);
			break;
			
		case "PARTICIPACAO-EM-EVENTOS-CONGRESSOS":
			createAndSetGroup("Participação em Eventos e Congressos");
			break;
			
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-SEMINARIO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-SEMINARIO":
			participationEventsConferences(attributes);
			break;
			
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-OFICINA":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-OFICINA":
			participationEventsConferences(attributes);
			break;
			
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-ENCONTRO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-ENCONTRO":
			participationEventsConferences(attributes);
			break;
			
		case "DADOS-BASICOS-DA-PARTICIPACAO-EM-EXPOSICAO":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DA-PARTICIPACAO-EM-EXPOSICAO":
			participationEventsConferences(attributes);
			break;
			
		case "DADOS-BASICOS-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			participationEventsConferences(attributes);
			break;
		case "DETALHAMENTO-DE-OUTRAS-PARTICIPACOES-EM-EVENTOS-CONGRESSOS":
			participationEventsConferences(attributes);
			break;
			
		case "1":
			break;
		case "2":
			break;
		case "3":
			break;
		case "40":
			break;
		case "50":
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
			
		default:
			break;
		}
	}

	/**
	 * evento characters do SAX. É onde podemos recuperar as informações texto
	 * contidas no documento XML (textos contidos entre tags). EX: <tag> texto
	 * recuperado </tag>
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);

	}

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

	private void concatTags(Attributes attributes, int index, String tagName) {
		if (attributes.getLocalName(index).equals(tagName)) {
			// para que assim seja possível passar adiante para a próxima tag
			baseToConcat = identifierEntry;
		} else {
			hashEntry.get(group).add(identifierEntry);
			entryCount++;
			System.out.println(String.format("Entrada %d adicionada ao grupo %s", entryCount, group)); // teste(?)
		}
	}

	private void createAndSetGroup(String group) {
		if (!hashEntry.containsKey(group)) {
			hashEntry.put(group, new ArrayList<>());
		}
		this.group = group;
	}

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
				identifierEntry += "/" + (attributes.getValue(i).isBlank() ? "--" : attributes.getValue(i));
			}
		}

		this.identifierEntry = identifierEntry;
	}

	private void buildCurriculum() {
		curriculum = new Curriculum();
		curriculum.setOwnerName(owner);
		curriculum.setEntryCount(entryCount);
		// setar entradas do currículo
		curriculum.setEntries(hashStringsToListEntries(hashEntry));
	}

	private List<Entry> hashStringsToListEntries(HashMap<String, List<String>> hashmap) {
		List<Entry> listEntry = new ArrayList<>();

		for (Map.Entry<String, List<String>> pair : hashmap.entrySet()) {

			for (String s : pair.getValue()) {
				Entry entry = new Entry();
				entry.setGroup(pair.getKey());
				entry.setName(s);
				listEntry.add(entry);
			}
		}

		return listEntry;
	}

	public Curriculum xmlToCurriculum(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
		doParse(xmlPath);
		return curriculum;
	}

	public static void main(String[] args) {
//		String path = "D:\\01-Documentos\\IF\\7 P\\PJ2\\curriculoXML-v2.xml";
		String path = "D:\\01-Documentos\\IF\\7 P\\PJ2\\teste.xml"; // para testes
		CurriculumXmlParseService cxps = new CurriculumXmlParseService();
		try {
			Curriculum c = cxps.xmlToCurriculum(path);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
