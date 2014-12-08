
/**
 * 
 */

/**
 * @author gaurav
 *
 */
public class LispGrammar {

	private static final SExpression NIL = new SExpression(Constants.NIL_TOKEN);
	private static final SExpression TRUE = new SExpression(Constants.TRUE);
	private static final SExpression QUOTE = new SExpression(Constants.QUOTE);
	private static final SExpression COND = new SExpression(Constants.COND);
	private static final SExpression DEFUN = new SExpression(Constants.DEFUN);
	private static final SExpression CAR = new SExpression(Constants.CAR);
	private static final SExpression CDR = new SExpression(Constants.CDR);
	private static final SExpression CONS = new SExpression(Constants.CONS);
	private static final SExpression ATOM = new SExpression(Constants.ATOM);
	private static final SExpression NULL = new SExpression(Constants.NULL);
	private static final SExpression EQ = new SExpression(Constants.EQ);
	private static final SExpression LESS = new SExpression(Constants.LESS);
	private static final SExpression GREATER = new SExpression(Constants.GREATER);
	private static final SExpression PLUS = new SExpression(Constants.PLUS);
	private static final SExpression TIMES = new SExpression(Constants.TIMES);
	private static final SExpression MINUS = new SExpression(Constants.MINUS);
	private static final SExpression QUOTIENT = new SExpression(Constants.QUOTIENT);
	private static final SExpression REMAINDER = new SExpression(Constants.REMAINDER);
	private static final SExpression INT = new SExpression(Constants.INT);
	private static SExpression declList = NIL;

	/**
	 * 
	 */
	public LispGrammar() {
	}

	public SExpression eval(SExpression expr) throws LispException{
		return eval(expr, NIL);
	}
	
	public SExpression eval(SExpression expr, SExpression list) throws LispException{
	    if (LispUtil.isIntAtom(expr) || LispUtil.isLiteralAtom(expr)) {
	        if(LispUtil.isIntAtom(expr))
	            return expr;
	        else if (containsInList(expr, list))
	            return getVal(expr, list);
	        //else if (expression->IsLiteral())
	         //   return expression;
	        else if (LispUtil.equal(expr, TRUE))
	            return TRUE;
	        else if (LispUtil.equal(expr, NIL))
	            return NIL;
	        else
	         throw new LispException("ERROR: EVAL -> Encountered an invalid input.");
	    }
	    else if(LispUtil.isLiteralAtom(LispUtil.car(expr))){
	        if(LispUtil.equal(LispUtil.car(expr), QUOTE)){
				if(LispUtil.isNil(LispUtil.cdr(expr)))
					throw new LispException("ERROR: EVAL -> Error evaluating QUOTE.");
				else if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(expr))))
					return LispUtil.car(LispUtil.cdr(expr));
				else 
					throw new LispException("ERROR: EVAL -> Multiple args to QUOTE.");
			}
	        else if (LispUtil.equal(LispUtil.car(expr), COND))
	            return evcon(LispUtil.cdr(expr), list);
	        else if (LispUtil.equal(LispUtil.car(expr),DEFUN))
	        {
	        	// Do sanity checks 
	            if(LispUtil.isNil(LispUtil.cdr(expr)))
	            	throw new LispException("ERROR: EVAL -> DEFUN should be followed by a function name");
	            else if (LispUtil.isIntAtom(LispUtil.car(LispUtil.cdr(expr))))
	            	throw new LispException("ERROR: EVAL -> Function name cannot be an INT.");
				else if(!LispUtil.isIntAtom(LispUtil.car(LispUtil.cdr(expr))) && !LispUtil.isLiteralAtom(LispUtil.car(LispUtil.cdr(expr))))
					throw new LispException("ERROR: EVAL -> Function name MUST be an atom");
				else if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(expr))))
					throw new LispException("ERROR: EVAL -> Invalid number of parameters for a DEFUN.");
				else if(!LispUtil.isList(LispUtil.car(LispUtil.cdr(LispUtil.cdr(expr)))))
					throw new LispException("ERROR: EVAL -> Function params is not a List.");
				else if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(LispUtil.cdr(expr)))))
					throw new LispException("ERROR: EVAL -> Function body cannot be NIL");
				else if(LispUtil.isValidName(LispUtil.car(LispUtil.cdr(expr))))
					throw new LispException("ERROR: EVAL -> Function name cannot be a reserved keyowrd.");
	            
	            SExpression funcName = LispUtil.car(LispUtil.cdr(expr));
	            SExpression params = LispUtil.car(LispUtil.cdr(LispUtil.cdr(expr)));
				SExpression funcBody = LispUtil.car(LispUtil.cdr(LispUtil.cdr(LispUtil.cdr(expr))));
	            
				SExpression func = LispUtil.cons(funcName, LispUtil.cons(params, funcBody));
				declList = LispUtil.cons(func, declList);
				return funcName;
	        }
	        else
	        {
	            SExpression evList = evlist(LispUtil.cdr(expr), list);
				if(evList == null)
					return null;
	            return apply(LispUtil.car(expr), evList, list);
			}
	    } else
	    	throw new LispException("ERROR: EVAL -> Invalid function call.");
	}
	
	public SExpression evcon(SExpression expr, SExpression list) throws LispException{
	    if (LispUtil.isNil(expr))
	    	throw new LispException("ERROR: EVCON -> NIL.");
	    if(LispUtil.isIntAtom(LispUtil.car(expr)) ||LispUtil.isLiteralAtom(LispUtil.car(expr)))
	    	throw new LispException("ERROR: EVCON -> Cond cannot be atom.");
	    SExpression cond = eval(LispUtil.car(LispUtil.car(expr)),list);
	    if (cond == null)
	    	throw new LispException("ERROR: EVCON -> Cond cannot be null.");
	    
	    if (!LispUtil.isLiteralAtom(cond) || (LispUtil.isLiteralAtom(cond) && !LispUtil.equal(cond,NIL))) {
	        if (LispUtil.isList(LispUtil.car(expr))) {
	            if (!LispUtil.isNil(LispUtil.cdr(LispUtil.car(expr))))
	                return eval(LispUtil.car(LispUtil.cdr(LispUtil.car(expr))),list);
	            else
	            	throw new LispException("ERROR: EVCON -> Expected a return value for each cond.");
	        } else
	        	throw new LispException("ERROR: EVCON -> Args to cond should be a list.");
	    }
	    return evcon(LispUtil.cdr(expr), list);
	}
	
	public SExpression evlist(SExpression expr, SExpression list) throws LispException{
		if (LispUtil.isNil(expr))
			return NIL;
		if (!LispUtil.isList(expr))
			return null;
		SExpression _tmp1 = eval(LispUtil.car(expr), list);
		SExpression _tmp2 = evlist(LispUtil.cdr(expr), list);
		if (_tmp1 == null || _tmp2 == null)
			return null;
		return LispUtil.cons(_tmp1, _tmp2);
	}
	
	public SExpression apply(SExpression expr, SExpression list){
		return null;
	}
	
	public SExpression getVal(SExpression expr, SExpression list) throws LispException{
		 if(LispUtil.isNil(list))
		        return NIL;
		 else if (LispUtil.equal(LispUtil.car(LispUtil.car(list)), expr))
		        return LispUtil.cdr(LispUtil.car(list));
		 else
		        return getVal(expr, LispUtil.cdr(list));
	}
	
	public SExpression addPair(SExpression expr, SExpression args, SExpression list) throws LispException{
		if(LispUtil.isNil(expr)) {
			if(LispUtil.isNil(args))
				return list;
			else
				throw new LispException("ERROR: ADDPAIR -> Arguments invalid.");
		}
		if((!LispUtil.isIntAtom(expr) || !LispUtil.isLiteralAtom(expr)) && (!LispUtil.isIntAtom(args) || !LispUtil.isLiteralAtom(args))) 
			list = LispUtil.cons(LispUtil.cons(LispUtil.car(expr),LispUtil.car(args)) , list);
		else
			throw new LispException("ERROR: ADDPAIR -> Invalid Expr/Args.");	    
		return addPair( LispUtil.cdr(expr),LispUtil.cdr(args), list);
	}
	
	public SExpression apply(SExpression func, SExpression args, SExpression list) throws LispException{
        if(LispUtil.isLiteralAtom(func) && LispUtil.isList(args)) {
            if(LispUtil.equal(func, CAR)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isLiteralAtom(LispUtil.car(args)) || (!LispUtil.isIntAtom(LispUtil.car(args)))){
                        if(LispUtil.isNil(LispUtil.cdr(args)))
                            return LispUtil.car(LispUtil.car(args));
                        else throw new LispException("ERROR: APPLY -> CAR allows only a single arg."); }
                    else throw new LispException("ERROR: APPLY -> CAR undefined for ATOMS."); }
                 else throw new LispException("ERROR: APPLY -> Illegeal usage of CAR."); }
            else if(LispUtil.equal(func, CDR)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isIntAtom(LispUtil.car(args)) || (!LispUtil.isLiteralAtom(LispUtil.car(args)))) {
                        if(LispUtil.isNil(LispUtil.cdr(args)))
                            return LispUtil.cdr(LispUtil.car(args));
                        else throw new LispException("ERROR: APPLY -> CDR allows only a single arg."); }
                    else throw new LispException("ERROR: APPLY -> CDR undefined for atoms."); }
                else throw new LispException("ERROR: APPLY -> Illegal use of CDR"); }
            else if(LispUtil.equal(func, CONS)) {
                if(!LispUtil.isNil(args)) {
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args))))
                            return LispUtil.cons(LispUtil.car(args),LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than two args for CONS"); }
                    else throw new LispException("ERROR: APPLY -> More than two args for CONS"); }
                else throw new LispException("ERROR: APPLY -> Illegal use of CONS"); }
            else if(LispUtil.equal(func, ATOM)) {
                if(!LispUtil.isNil(args)) {
                    if(LispUtil.isNil(LispUtil.cdr(args))) {
                        if(LispUtil.isLiteralAtom(LispUtil.car(args)) || (LispUtil.isIntAtom(LispUtil.car(args))))
                            return TRUE;
                        else
                            return NIL;
                    }
                    else throw new LispException("ERROR: APPLY -> More than one arg for ATOM"); }
                else throw new LispException("ERROR: APPLY -> No arg provided for ATOM"); }
            else if(LispUtil.equal(func, NULL)) {
                if(!LispUtil.isNil(args)){
                    if(LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.car(args)))
                            return TRUE;
                        else
                            return NIL;
                    }
                    else throw new LispException("ERROR: APPLY -> More than one arg for NIL"); }
                else throw new LispException("ERROR: APPLY -> No arg provided for NIL"); }
            else if(LispUtil.equal(func, EQ)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) {
                            boolean _res = LispUtil.equal(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                            if(_res)
                                return TRUE;
                            else if(!_res)
                                return NIL;
                        }
                        else throw new LispException("ERROR: APPLY -> More than two args for EQ"); }
                    else throw new LispException("ERROR: APPLY -> More than two arg for EQ"); }
                else throw new LispException("ERROR: APPLY -> No arg for EQ"); }
            else if(LispUtil.equal(func, LESS)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) {
                        	boolean _res = LispUtil.less(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        	if(_res)
                                return TRUE;
                            else if(!_res)
                                return NIL;
                        }
                        else throw new LispException("ERROR: APPLY -> More than two args for LESS"); }
                    else throw new LispException("ERROR: APPLY -> More than two arg for LESS"); }
                else throw new LispException("ERROR: APPLY -> No arg for LESS"); }
            else if(LispUtil.equal(func, GREATER)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) {
                        	boolean _res = LispUtil.greater(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        	if(_res)
                                return TRUE;
                            else if(!_res)
                                return NIL;
                        }
                        else throw new LispException("ERROR: APPLY -> More than two args for GREATER"); }
                    else throw new LispException("ERROR: APPLY -> More than two arg for GREATER"); }
                else throw new LispException("ERROR: APPLY -> No arg for GREATER"); }
            else if(LispUtil.equal(func, INT)){
                if(!LispUtil.isNil(args)){
                    if(LispUtil.isNil(LispUtil.cdr(args))) {
                        if(LispUtil.isIntAtom(LispUtil.car(args)))
                            return TRUE;
                        else
                            return NIL;
                    }
                    else throw new LispException("ERROR: APPLY -> More than one args for INT"); }
                else throw new LispException("ERROR: APPLY -> No arg provided for GREATER"); }
            else if(LispUtil.equal(func, PLUS)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) 
                            return  LispUtil.plus(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than one args for PLUS"); }
                    else throw new LispException("ERROR: APPLY -> Two args required for PLUS"); }
                else throw new LispException("ERROR: APPLY -> More than one args for PLUS"); }
            else if(LispUtil.equal(func, MINUS)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) 
                            return  LispUtil.minus(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than one args for MINUS"); }
                    else throw new LispException("ERROR: APPLY -> Two args required for MINUS"); }
                else throw new LispException("ERROR: APPLY -> No args for MINUS"); }
            else if(LispUtil.equal(func, TIMES)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) 
                            return  LispUtil.times(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than one args for TIMES"); }
                    else throw new LispException("ERROR: APPLY -> Two args required for TIMES"); }
                else throw new LispException("ERROR: APPLY -> No args provided for TIMES"); }
            else if(LispUtil.equal(func, QUOTIENT)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) 
                            return  LispUtil.quotient(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than one args for Quotient"); }
                    else throw new LispException("ERROR: APPLY -> Two args required for Quotient"); }
                else throw new LispException("ERROR: APPLY -> No args provided for Quotient"); }
            else if(LispUtil.equal(func, REMAINDER)){
                if(!LispUtil.isNil(args)){
                    if(!LispUtil.isNil(LispUtil.cdr(args))){
                        if(LispUtil.isNil(LispUtil.cdr(LispUtil.cdr(args)))) 
                            return  LispUtil.remainder(LispUtil.car(args), LispUtil.car(LispUtil.cdr(args)));
                        else throw new LispException("ERROR: APPLY -> More than one args for Remainder"); }
                    else throw new LispException("ERROR: APPLY -> Two args required for Remainder"); }
                else throw new LispException("ERROR: APPLY -> No args provided for Remainder"); }
            else {
                SExpression _val = getVal(func, declList);
                if(LispUtil.isNil(_val)){
                	LispUtil.printTree(_val);
                	throw new LispException(" \nERROR: Undefined method name.");
                }
                SExpression _body = LispUtil.cdr(_val);
                SExpression _params = LispUtil.car(_val);
                
                list = addPair(_params, args, list);
                
                if(list == null)
                    throw new LispException("ERROR: APPLY -> List is null.");
                
                return eval(_body,list);
            }
        }
        else 
        	throw new LispException("ERROR: Not a valid function call.");
        return null;
}
	
	public boolean containsInList(SExpression expr, SExpression list) throws LispException{
		if(LispUtil.isNil(list))
			return false;
		else if(LispUtil.equal(LispUtil.car(LispUtil.car(list)), expr))
			return true;
		else
			return containsInList(expr, LispUtil.cdr(list));
	}
}