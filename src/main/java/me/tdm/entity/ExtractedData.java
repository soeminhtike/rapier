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

}
