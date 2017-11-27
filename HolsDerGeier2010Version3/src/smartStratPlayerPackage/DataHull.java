package smartStratPlayerPackage;

public class DataHull {
	private InfoLevel infoLevel;
	private NormalForm normalForm;
	private TransformedNormalForm transformedNormalForm;
	
	public InfoLevel getInfoLevel() {
		return infoLevel;
	}
	public void setInfoLevel(InfoLevel infoLevel) {
		this.infoLevel = infoLevel;
	}
	public NormalForm getNormalForm() {
		return normalForm;
	}
	public void setNormalForm(NormalForm normalForm) {
		this.normalForm = normalForm;
	}
	public TransformedNormalForm getTransformedNormalForm() {
		return transformedNormalForm;
	}
	public void setTransformedNormalForm(TransformedNormalForm transformedNormalForm) {
		this.transformedNormalForm = transformedNormalForm;
	}
	
	
	@Override
	public String toString(){
		String res = "";
		res+="T:={";
		for(Integer i : infoLevel.getAnimalCardDeck()){
			res += i + ",";
		}
		res+="}";
		
		res += "CC = " + infoLevel.getCurrentAnimalCard();
		
		res+= " A:= {";
		for(Integer i : infoLevel.getPlayerADeck()){
			res+= i + ",";
		}
		res+="}";
		res+=" B:={";
		for(Integer i : infoLevel.getPlayerBDeck()){
			res+=i+",";
		}
		res += "}";
		res += " " + "SD= " + infoLevel.getScoreDelta();
		res += " " + "DS= " + infoLevel.getDrawPoints();
		return res;
	}
	
}
