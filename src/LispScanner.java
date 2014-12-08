import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 
 */

/**
 * @author gaurav
 *
 */
public class LispScanner {
	boolean incomplete;
	ArrayList<String> tokens;
	Stack<Character> paren;
	
	public LispScanner(){
		this.incomplete=true;
		tokens = new ArrayList<String>();
		paren = new Stack<Character>();
	}
	
	/**
	 * Scan the input and separate out the tokens
	 * @param input
	 * @return
	 * @throws LispException 
	 */
	public void scan(String input) throws LispException {
		for(int i=0;i<input.length();i++){
			if(input.charAt(i) == Constants.OPEN_BRACKET){
				tokens.add("("); 
				paren.push('(');
				incomplete=true;
				continue;
			}else if(input.charAt(i) == Constants.CLOSED_BRACKET){
				tokens.add(")");
				try{
					paren.pop();
				}catch (EmptyStackException e){
					throw new LispException("ERROR: SCAN_ERROR->Encountered more ')' than '('.");
				}
				incomplete=!paren.isEmpty();
				continue;
			}else if(input.charAt(i) == Constants.DOT){
				if(input.charAt(i-1) != Constants.SPACE || input.charAt(i+1) != Constants.SPACE)
					throw new LispException("ERROR: SCAN_ERROR->No spaces before DOT.");
				tokens.add(" . "); 
				continue;
			}else if(input.charAt(i) == Constants.SPACE || input.charAt(i) == Constants.TAB_SPACE || input.charAt(i) == Constants.NEW_LINE)
				//ignore spaces
				continue;
			else{
				//it is a string or integer token
				int j=i;
				while (j < input.length() &&
						input.charAt(j) != Constants.OPEN_BRACKET
						&& input.charAt(j) != Constants.CLOSED_BRACKET
						&& input.charAt(j) != Constants.DOT
						&& input.charAt(j) != Constants.SPACE
						&& input.charAt(j) != Constants.TAB_SPACE) {
					j++;
				}
				tokens.add(input.substring(i, j).trim());
				i=j-1;
			}		
		}
		if(paren.empty())
			incomplete=false;
	}

}
