package com.orange.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	private IndexWriter writer;
	private IndexWriter chineseIndexWriter;
	private Directory indexDirectory;

	public static void main(String[] args) throws Exception {
		String conent = "电脑型号:联想 20FNA06FCD 笔记本电脑,处理器:英特尔 Core i7-6500U @ 2.50GHz 双核,厉害了我的国";
		Analyzer analyzer = new StandardAnalyzer();
		SmartChineseAnalyzer sca = new SmartChineseAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", conent);
		doToken(ts);
		TokenStream tsca = sca.tokenStream("conent", conent);
		doToken(tsca);
//		Indexer indexer = new Indexer();
//		indexer.createIndex("D:/lucene", "D:\\logs");
	}

	private static void doToken(TokenStream ts) throws IOException {
		ts.reset();
		CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
		while(ts.incrementToken()) {
			System.out.print(cta.toString() + "|");
		}
		System.out.println();
		ts.end();
		ts.close();
	}

	@SuppressWarnings("deprecation")
	public void createIndex(String indexDirectoryPath, String srcDirectory) throws Exception {
		indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		// 设置分词器
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(indexDirectory, config);
		// 设置中文分词器
		SmartChineseAnalyzer sca = new SmartChineseAnalyzer();
		IndexWriterConfig chineseWriterConfig = new IndexWriterConfig(sca);
		chineseIndexWriter = new IndexWriter(indexDirectory, chineseWriterConfig);
		
		// 创建索引文件
		File files = new File(srcDirectory);
		for (File file : files.listFiles()) {
			String  fileName = file.getName();
			// 文件内容
			String fileContent = FileUtils.readFileToString(file);
			String filePath = file.getPath();
			long fileSize = FileUtils.sizeOf(file);
			System.out.println("filePath" + fileName);
			Document document = new Document();
			document.add(new TextField("name", fileName, Store.YES));
			document.add(new TextField("content", fileContent, Store.YES));
			document.add(new StoredField("path", filePath));
			document.add(new StoredField("fileSize", fileSize));
			writer.addDocument(document);
		}
		
		writer.close();
	}
	
}
