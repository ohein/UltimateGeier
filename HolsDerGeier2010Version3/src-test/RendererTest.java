import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import player.HolsDerGeierSpieler;
import player.StupidBot;
import vultureUtil.BuzzardAnalyser;
import vultureUtil.BuzzardGameModel;
import vultureUtil.BuzzardRenderer;
import vultureUtil.TableFactory;

@RunWith(value=Parameterized.class)
public class RendererTest {
	
	private final static Color LIGHT_GRAY = new Color(220,220,220);
	private final static Color DARK_GRAY = Color.LIGHT_GRAY;
	private final static Color YELLOW = Color.YELLOW;
	private final static Color GREEN = Color.GREEN;
	private final static Color RED = Color.RED;
	private final static Color WHITE = Color.WHITE;
	
	private static JTable testTable;
	private static BuzzardRenderer renderer;
	private Color expected;
	private int row;
	private int column;
	
	public RendererTest(ParameterObject po){
		expected = po.color;
		row = po.row;
		column = po.column;
	}
	
	@Test
	public void checkColor(){
		Color received = ((JLabel) renderer.getTableCellRendererComponent(
		        testTable, 
		        testTable.getValueAt(row, column), 
		        false, false, row, column)).getBackground();
		        
		assertEquals("Unerwartete Zellenfarbe: expexted" + expected + "got: " + received, expected, received);	
	}
	
	@BeforeClass
	public static void setUp(){
		HolsDerGeierSpieler[] spieler = {
				new StupidBot("Spieler1"),
				new StupidBot("Spieler2"),
				new StupidBot("Spieler3")};
		BuzzardAnalyser analyser = new BuzzardAnalyser();
		BuzzardGameModel model = new BuzzardGameModel(spieler,4,analyser);
		renderer = new BuzzardRenderer(model);
	    testTable = TableFactory.getJTable(model);
		
		Object[][] data = {
		   {1,-1,1,-1}, // Tierkartenzeile
		   {3,3,3,2},  // Spieler 1
		   {2,2,3,2}, // Spieler 2
		   {1,1,1,1}}; // Spieler 3
		
		for(int i = 0; i < data.length;i++){
			for(int j = 0; j < data[0].length; j++){
				model.setValueAt(((Integer)data[i][j]).toString(), i, j+1);
			}
		}
	}
	
	@Parameters
	public static Collection<ParameterObject[]> getTestParameters(){
//		Collection<ParameterObject[]> res = new ArrayList<ParameterObject[]>();
//		ParameterObject[] resArray = new ParameterObject[1];
//		resArray[0] = new ParameterObject(Color.GREEN,1,0);
//		res.add(resArray);
//		return res
		
		return Arrays.asList(new ParameterObject[][]{
				{new ParameterObject(LIGHT_GRAY,0,0)},
				{new ParameterObject(LIGHT_GRAY,0,1)},
				{new ParameterObject(LIGHT_GRAY,0,2)},
				{new ParameterObject(LIGHT_GRAY,0,3)},
				{new ParameterObject(LIGHT_GRAY,0,4)},
				{new ParameterObject(DARK_GRAY,1,0)},
				{new ParameterObject(GREEN,1,1)},
				{new ParameterObject(WHITE,1,2)},
				{new ParameterObject(YELLOW,1,3)},
				{new ParameterObject(WHITE,1,4)},
				{new ParameterObject(DARK_GRAY,2,0)},
				{new ParameterObject(WHITE,2,1)},
				{new ParameterObject(WHITE,2,2)},
				{new ParameterObject(YELLOW,2,3)},
				{new ParameterObject(WHITE,2,4)},
				{new ParameterObject(DARK_GRAY,3,0)},
				{new ParameterObject(WHITE,3,1)},
				{new ParameterObject(WHITE,2,2)},
				{new ParameterObject(YELLOW,2,3)},
				{new ParameterObject(WHITE,2,4)},
				{new ParameterObject(DARK_GRAY,3,0)},
				{new ParameterObject(WHITE,3,1)},
				{new ParameterObject(RED,3,2)},
				{new ParameterObject(GREEN,3,3)},
				{new ParameterObject(RED,3,4)}
			}
		);
	}

	static class ParameterObject{
		Color color;
		int row;
		int column;
		ParameterObject(Color color, int row, int column){
			this.color = color;
			this.row = row;
			this.column = column;
		}
	}
}
