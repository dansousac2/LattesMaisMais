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

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;

public class CurriculumXmlParseService extends DefaultHandler {

	private Curriculum curriculum;
	private String owner;
	private int entryCount = 0;
	private HashMap<String, List<String>> hashEntry;
	private String lastElement;
	private String group = "";
	private Attributes attribOfLastElem;

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
		
		// aLternativa 2 - modificando o decodificador para reconhecer o padrão do XML (ISO-8859-1),
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
		System.out.println("- - - Início de parse de documento - - -");
		hashEntry = new HashMap<>();
	}

	public void endDocument() throws SAXException {
		super.endDocument();
		// preparando currículo
		buildCurriculum();
		System.out.println("\n\n- - - Fim de parse de documento! - - -");
	}
	
	/**
	 * evento startElement do SAX. disparado quando o processador SAX identifica
	 * a abertura de uma tag. Ele possibilita a captura do nome da tag e dos
	 * nomes e valores de todos os atributos associados a esta tag, caso eles
	 * existam.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		// Nome e valor do primeiro atributo da tag (cada)
		String firstAttributeName = attributes.getLocalName(0);
		// pegando nome do proprietário do currículo
		if(localName.equals("DADOS-GERAIS")) {
			owner = attributes.getValue(0);
			System.out.println("Currículo pertencente a: " + owner + "\n");
		// demais casos analizasse a entrada identificada pelo padrão "SEQUENCIA"
		} else if(firstAttributeName != null && firstAttributeName.contains("SEQUENCIA-")) { 
			// identificando o grupo pertencente (específico no XML analisado)
			if(attribOfLastElem.getLocalName(0) == null || !attribOfLastElem.getLocalName(0).contains("SEQUENCIA-") && !group.equals(lastElement)) {
				group = lastElement;
				hashEntry.put(group, new ArrayList<String>());
			}
			// adiciona-se algum valor ao hashmap na chave "group", criando um novo ou aproveitando um grupo já existente
			hashEntry.get(group).add(formatEntry(attributes));
			System.out.println(String.format("Entrada %d adicionada ao grupo => %s", ++entryCount, group));
		}
		// quando uma tag é analiasda ela é colocada como última e tem seu primeiro atributo guardado para verificação (possível grupo)
		lastElement = localName;
		// os atributos não são modificados se apenas apontam para os atributos recebidos, sendo necessário instanciar.
		attribOfLastElem = new AttributesImpl(attributes);
	}
	
	/**
	 * evento endElement do SAX. Disparado quando o processador SAX identifica o
	 * fechamento de uma tag (ex: </nome>)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		
	}


	/**
	 * evento characters do SAX. É onde podemos recuperar as informações texto
	 * contidas no documento XML (textos contidos entre tags). EX: <tag> texto recuperado </tag>
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		
	}
	// formata cada entrada identificada para uma única String
	private String formatEntry(Attributes attributes) {
		String entry = "";
		// em alguns casos, a tag anterior guarda informações que devem ser recuperadas.
		// nesse caso, cria-se uma lista de Attrubutes com mais de 1 Attribute
		List<Attributes> attribList = new ArrayList<>();
		if(attribOfLastElem.getLocalName(0) != null) {
			attribList.add(attribOfLastElem);
		}
		attribList.add(attributes);
		
		for(Attributes att : attribList) {
			for(int i = 0; att.getValue(i) != null; i++) {
				if(att.getValue(i) != "") {
					entry += "/_/" + att.getValue(i);
				}
			}
		}
		// removendo alguns elementos desnecessários
		String filtredEntry = removeUnnecessaryElements(entry);
		System.out.println("\n" + filtredEntry); // mostrar entradas no console
		
		return filtredEntry;
	}
	
	private String removeUnnecessaryElements(String entry) {
		String filtredEntry = "";
		
		// transformamos a String em um Array separando elementos por "/"
		String[] listWords = entry.split("/_/");
		for(String s : listWords) {
			if(s.length() < 4) {
				continue;
			// caso seja número e possua mais de 4 dígitos, não entra para a seleção
			} else if(s.matches("^[0-9]{5,}$")) {
				continue;
			}
			
			filtredEntry += "/" + s;
		}
		
		return filtredEntry;
	}

	private void buildCurriculum() {
		curriculum = new Curriculum();
		curriculum.setOwner(owner);
		curriculum.setEntryCount(entryCount);
		// setar entradas do currículo
		curriculum.setEntries(hashStringsToListEntries(hashEntry));
	}
	
	private List<Entry> hashStringsToListEntries(HashMap<String, List<String>> hashmap) {
		List<Entry> listEntry = new ArrayList<>();
		
		for(Map.Entry<String, List<String>> pair : hashmap.entrySet()) {
			
			for(String s : pair.getValue()) {
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
			Curriculum curriculum = cxps.xmlToCurriculum(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
