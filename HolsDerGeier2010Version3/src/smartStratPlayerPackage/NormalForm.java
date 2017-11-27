package smartStratPlayerPackage;

public class NormalForm {
	Double[][] data;
	String[] rowHeader;
	String[] colHeader;
	
	public Double[][] getData() {
		return data;
	}
	public void setData(Double[][] data) {
		this.data = data;
	}
	public String[] getRowHeader() {
		return rowHeader;
	}
	public void setRowHeader(String[] rowHeader) {
		this.rowHeader = rowHeader;
	}
	public String[] getColHeader() {
		return colHeader;
	}
	public void setColHeader(String[] colHeader) {
		this.colHeader = colHeader;
	}
	
	public Integer getNumberOfRows(){
		return data.length;
	}
	
	public Integer getNumberOfCols(){
		return data[0].length;
	}
	
}
