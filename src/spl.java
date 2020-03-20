public class spl 
{ 
	private File file;
	private FileReader fileReader;
    private Lexer lexer;

    //!Compiler uses the passed in filename - expected to be within current directory
    public spl(String file) 
    { 
    	this.file = new File(file);
    	this.fileReader = new FileReader(this.file);
		this.lexer = new Lexer(new BufferedReader(this.fileReader));
    } 
  
    public static void main(String[] args) 
    { 
        
    } 
}