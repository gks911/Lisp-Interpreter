import java.util.ArrayList;
import java.util.Stack;

/**
 * @author gaurav
 *
 */
public class LispParser {
	/**
	 * Method which accepts tokens from the output of the scanner and
	 * recursively builds a parse tree for the expressions
	 * @param tokens
	 * @return
	 * @throws LispException
	 */
	public SExpression getParseTree(ArrayList<String> tokens)
			throws LispException {

		int _current = 0;
		int _len = tokens.size();
		ArrayList<String> _leftList = new ArrayList<String>();
		ArrayList<String> _rightList = new ArrayList<String>();
		SExpression _left = null;
		SExpression _right = null;

		if (tokens.get(0).equals(Constants.OPEN_BRACKET.toString())) {
			_current++;
			Stack<Character> paren = new Stack<Character>();

			if (tokens.get(_current).equals(Constants.DOT.toString()))
				throw new LispException("ERROR: PARSE_ERROR->Invalid usage of DOT '.'");

			if (tokens.get(_current).equals(Constants.OPEN_BRACKET.toString())) {
				_leftList.add(tokens.get(_current));
				paren.push(Constants.OPEN_BRACKET);
				_current++;
				while (!paren.isEmpty() && _current < _len) {
					if (tokens.get(_current).equals(Constants.OPEN_BRACKET.toString()))
						paren.push(Constants.OPEN_BRACKET);
					else if (tokens.get(_current).equals(Constants.CLOSED_BRACKET
							.toString()))
						paren.pop();

					_leftList.add(tokens.get(_current));
					_current++;
				}
				_left = getParseTree(_leftList);
			} else if (tokens.get(_current).equals(Constants.CLOSED_BRACKET.toString()))
				return getNewSExpressionFromString(Constants.NIL_TOKEN);
			else if (tokens.get(_current).equals(Constants.SPACED_DOT))
				throw new LispException("ERROR: PARSE_ERROR-> Invalid SExpression");
			else {
				_left = getNewSExpressionFromString(tokens.get(_current));
				_current++;
			}

			boolean isAList = true;
			if (tokens.get(_current).equals(Constants.SPACED_DOT)) {
				isAList = false;
				_current++;
			}

			while (_current < _len) {
				if (tokens.get(_current).equals(Constants.DOT.toString())) 
					throw new LispException("ERROR: PARSE_ERROR->Invalid usage of DOT '.'");
				_rightList.add(tokens.get(_current));
				_current++;
			}

			if (_rightList.size() == 0) 
				throw new LispException("ERROR: PARSE_ERROR->Invalid SExpression.");

			if (isAList)
				_right = getListAsSExpression(_rightList);
			else
				_right = getNewSExpressionFromList(_rightList);
			if (_left == null || _right == null)
				return null;

			return new SExpression(_left, _right); //END_OF_IF
		} else if (!tokens.get(0).equals(Constants.CLOSED_BRACKET.toString())
				&& !tokens.get(0).equals(Constants.SPACED_DOT))
			return getNewSExpressionFromString(tokens.get(_current));
		else {
			throw new LispException(
					"ERROR: PARSE_ERROR-> SExpression cannot start with closed bracket or dot");
		}
	}

	private SExpression getNewSExpressionFromList(ArrayList<String> tokens)
			throws LispException {
		if (!tokens.get(tokens.size()-1).equals(Constants.CLOSED_BRACKET.toString()))
			throw new LispException("ERROR: PARSE_ERROR-> SExpression has a missing ')'");

		tokens.remove(tokens.size()-1);
		String _token = new String();

		if (!tokens.isEmpty())
			_token = tokens.get(0);

		if (tokens.size() == 1
				&& !_token.equals(Constants.OPEN_BRACKET.toString())
				&& !_token.equals(Constants.CLOSED_BRACKET.toString())
				&& !_token.equals(Constants.SPACED_DOT))
			return getNewSExpressionFromString(_token);
		else if (tokens.size() > 1
				&& _token.equals(Constants.OPEN_BRACKET.toString()))
			return getParseTree(tokens);
		else
			throw new LispException("ERROR: PARSE_ERROR-> Invalid SExpression");
	}

	private SExpression getNewSExpressionFromString(String token) throws LispException {
		int _intValue = 0;
		int _current = 0;
		boolean _isNegative=false;

		//It's a string literal. Do sanity checks
		if (Character.isLetter(token.charAt(_current))) {
			while(_current < token.length()){	
				if (!((Character.isLetter(token.charAt(_current))) || (token.charAt(_current) >= '0' && token
						.charAt(_current) <= '9')))
					throw new LispException("ERROR: PARSE_ERROR-> Invalid identifier '" + token
							+ "'");
				_current++;
			}
			return (new SExpression(token));
		} else if (token.charAt(_current) == '+') {
			_current++;
		} else if (token.charAt(_current) == '-') {
			_isNegative=true;
			_current++;
		}

		//It's an integer 
		if (_current < token.length() && token.charAt(_current) >= '0'
				&& token.charAt(_current) <= '9') {
			while(_current < token.length()){
				if (token.charAt(_current) >= '0' && token.charAt(_current) <= '9')
					//convert to the number
					_intValue = 10 * _intValue + (token.charAt(_current) - '0');
				else
					throw new LispException("ERROR: PARSE_ERROR-> Invalid input '"+token+"'");
				_current++;
			}
			if(_isNegative)
				return (new SExpression(_intValue * -1));
			else
				return (new SExpression(_intValue));
		}
		throw new LispException("ERROR: PARSE_ERROR-> Invalid input token '" + token + "'");
	}

	private SExpression getListAsSExpression(ArrayList<String> tokens) throws LispException {
		int _len = tokens.size();
		ArrayList<String> _leftList = new ArrayList<String>();
		ArrayList<String> _rightList = new ArrayList<String>();
		SExpression _left;
		SExpression _right;

		int _current = 0;
		if (tokens.size() == 1) {
			if (tokens.get(0).equals(Constants.CLOSED_BRACKET.toString()))
				return getNewSExpressionFromString(Constants.NIL_TOKEN);
			else
				throw new LispException("ERROR: PARSE_ERROR-> Invalid List in SExpression");
		} else {
			if (tokens.get(0).equals(Constants.OPEN_BRACKET.toString())) {
				Stack<Character> paren = new Stack<Character>();
				paren.push(Constants.OPEN_BRACKET);
				_leftList.add(tokens.get(_current));
				_current++;

				while (!paren.isEmpty() && _current < _len) {
					if (tokens.get(_current).equals(Constants.DOT.toString())) 
						throw new LispException("ERROR: PARSE_ERROR->  Invalid usage of DOT '.' ");

					if (tokens.get(_current).equals(Constants.OPEN_BRACKET.toString()))
						paren.push(Constants.OPEN_BRACKET);
					else if (tokens.get(_current).equals(Constants.CLOSED_BRACKET.toString()))
						paren.pop();

					_leftList.add(tokens.get(_current));
					_current++;
				}
				_left = getParseTree(_leftList);
			} else if (!tokens.get(_current).equals(Constants.CLOSED_BRACKET.toString())
					&& !tokens.get(_current).equals(Constants.SPACED_DOT)) {
				_left = getNewSExpressionFromString(tokens.get(_current));
				_current++;
			} else throw new LispException("ERROR: PARSE_ERROR-> Invalid SExpression");

			while (_current < _len) {
				if (tokens.get(_current).equals(Constants.DOT.toString()))
					throw new LispException("ERROR: PARSE_ERROR-> Invalid usage of DOT '.'");

				_rightList.add(tokens.get(_current));
				_current++;
			}
				if (_rightList.size() == 0)
					throw new LispException("ERROR: PARSE_ERROR-> Invalid SExpression");
				_right = getListAsSExpression(_rightList);

				if (_left == null || _right == null)
					return null;
				return new SExpression(_left, _right);
			}
		}
}