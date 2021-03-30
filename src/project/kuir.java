package project;

public class kuir {
	public static void main(String[] args) throws Exception {
		
		if(args!=null) {
			if(args[0].equals("c")) {
				makeCollection col = new makeCollection(args[1]);		// "src/html/"
				col.callDocument();
				col.convertHTMLtoXML();
				col.printXML();
			}else if(args[0].equals("k")) {
				makeKeyword key = new makeKeyword(args[1]);				// ../collection.xml
				key.countKeyword();
				key.printXML();
			}else if(args[0].equals("i"));{
				indexer idx = new indexer(args[1]);						// ../index.xml
				idx.calculateTfIdf();
			}
		}
	}
}
















