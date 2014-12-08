import java.util.Scanner;

/**
 * @author gaurav
 *
 */
public class Interpreter {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		LispScanner lScanner = new LispScanner();
		LispParser lParser = new LispParser();
		LispGrammar grammar = new LispGrammar();
		boolean _complete=false;

		while (scanner.hasNext()) {
			try {
				String input = scanner.nextLine();
				if (input.isEmpty())
					continue;
				//Lex Analyze: Tokenize the input
				lScanner.scan(input.toUpperCase());
				//Continue to get user input in case we still require more tokens
				if (lScanner.incomplete)
					continue;
				else{
					//Parser: Build the parse tree, and get the root
					SExpression _sexp = lParser.getParseTree(lScanner.tokens);
					LispUtil.printTree(_sexp);
					System.out.print("\n");
					_complete=true;
					//Evaluate the expression and print the result
					SExpression res = grammar.eval(_sexp);
					if (res == null)
						System.out.println();
					else
						LispUtil.printTree(res);
					lScanner = new LispScanner();
				}
			} catch (LispException e) {
				_complete=true;
				System.out.println(e.getMessage());
			}catch (Exception e2){
				_complete=true;
				System.out.println("ERROR: INVAID INPUT?");
			}
			finally{
				if(_complete)
					lScanner = new LispScanner();
			}
		}
		scanner.close();
	}
}
