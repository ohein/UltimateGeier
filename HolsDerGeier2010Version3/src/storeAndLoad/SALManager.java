package storeAndLoad;

import java.io.File;



public class SALManager {
	
	public static void loadAll(File file)throws Exception{
		if(!(file.isDirectory())){
			throw new Exception("File must be a directory.");
		}
		Class<?> c;
		String[] files = file.list();
		String fileNameWhole;
		String fileName;
		String fileType;
		int index;
		for(int i=0;i<files.length;i++){
			fileNameWhole = files[i];
			index = fileNameWhole.lastIndexOf(".");
			fileName=fileNameWhole.substring(0,index);
			fileType=fileNameWhole.substring(index+1);
			System.out.println(fileName);
			System.out.println(fileType);
			if(index==-1 || fileType.equals("class")){
				continue;
			}else{
				MultiLoader.loadClass(file, fileName);
			}


		}
	}
	
	public static void sal(File file){
		ClassStorer.storeClass(file, "src\\cls");
		MultiLoader.newInstance(file);
	}
}
