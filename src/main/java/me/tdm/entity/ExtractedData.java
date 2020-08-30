package me.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "extracteddata")
public class ExtractedData extends BaseEntity {

	@OneToOne
	private DataEntry dataEntry;

	private String title;

	private String author;

	private String isbn;
	
	private String paperBack;
	
	private String price;
	
	private String edition;
	
	private String publication;
	
	private String publisher;

	public DataEntry getDataEntry() {
		return dataEntry;
	}

	public void setDataEntry(DataEntry dataEntry) {
		this.dataEntry = dataEntry;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getPaperBack() {
		return paperBack;
	}

	public void setPaperBack(String paperBack) {
		this.paperBack = paperBack;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public static ExtractedData create() {
		return new ExtractedData();
	}

}
