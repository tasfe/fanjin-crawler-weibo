package ort.firephone.SinaUtils.db;

public class Joke extends Fdb {
	public static String default_table_name = "joke";
	private int size;
	private String cat;
	private String title;
	private String content;
	private String source;

	public void insertIntoDb() {
		String sql = "insert into " + default_table_name + " set ";
		sql += item("size", size) + " ,";
		sql += item("cat", cat) + " ,";
		sql += item("source", source) + " ,";
		sql += item("title", title) + " ,";
		sql += item("content", content) + " ";
		getDb().executequery(sql);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
