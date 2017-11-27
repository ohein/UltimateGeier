
public class Schwachsinnsklasse {
	public static void main(String[] args ){
		Object[][] data = {
				   {1,3,2,1},
				   {-1,3,2,1},
				   {1,3,3,1},
				   {-1,3,3,1}};
				
				for(int i = 0; i < data.length;i++){
					for(int j = 0; j < data[0].length; j++){
						System.out.print(data[i][j]);
					}
					System.out.println();
				}
	}
}
