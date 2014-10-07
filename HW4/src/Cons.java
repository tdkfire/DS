/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          02 Oct 09; 12 Feb 10; 04 Oct 12
 */

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }

    // convert a list to a string for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public static int square(int x) { return x*x; }

    // iterative destructive merge using compareTo
public static Cons dmerj (Cons x, Cons y) {
  if ( x == null ) return y;
   else if ( y == null ) return x;
   else { Cons front = x;
          if ( ((Comparable) first(x)).compareTo(first(y)) < 0)
             x = rest(x);
            else { front = y;
                   y = rest(y); };
          Cons end = front;
          while ( x != null )
            { if ( y == null ||
                   ((Comparable) first(x)).compareTo(first(y)) < 0)
                 { setrest(end, x);
                   x = rest(x); }
               else { setrest(end, y);
                      y = rest(y); };
              end = rest(end); }
          setrest(end, y);
          return front; } }

public static Cons midpoint (Cons lst) {
  Cons current = lst;
  Cons prev = current;
  while ( lst != null && rest(lst) != null) {
    lst = rest(rest(lst));
    prev = current;
    current = rest(current); };
  return prev; }

    // Destructive merge sort of a linked list, Ascending order.
    // Assumes that each list element implements the Comparable interface.
    // This function will rearrange the order (but not location)
    // of list elements.  Therefore, you must save the result of
    // this function as the pointer to the new head of the list, e.g.
    //    mylist = llmergesort(mylist);
public static Cons llmergesort (Cons lst) {
  if ( lst == null || rest(lst) == null)
     return lst;
   else { Cons mid = midpoint(lst);
          Cons half = rest(mid);
          setrest(mid, null);
          return dmerj( llmergesort(lst),
                        llmergesort(half)); } }


    // ****** your code starts here ******
    // add other functions as you wish.

public static Cons union (Cons x, Cons y) {
	return mergeunion(llmergesort(x), llmergesort(y));
}

// following is a helper function for union
	public static Cons mergeunion (Cons x, Cons y) {
		if(x == null && y == null){
			return null;
		}	
		else if ( x == null) {
			return cons(first(y), mergeunion(x, rest(y)));
		}
		else if(y == null){
			return cons(first(x), mergeunion(rest(x), y));
		}
		   else if ( first(x).equals(first(y)) ){
	           return cons(first(x),mergeunion(rest(x),rest(y)));
	
		   }
		  else if ( ((Comparable) first(x)).compareTo(first(y)) < 0){
		      return cons(first(x),mergeunion(rest(x), y));
	
		  }
		  else return cons(first(y),mergeunion(x, rest(y)));
	}
private static Cons duplicates(Cons lst, Cons result){
	if(lst == null){
		return result;
	}
	else{
		if(!first(lst).equals(first(rest(lst)))){
			result = cons(first(lst), result);
		}
	}
	return duplicates(rest(lst), result);
}


private static boolean isEqual(Cons x, Cons y){
	boolean equality = false;
	
	while(first(x) != null && first(y) !=null){
		if(first(x).equals(first(y))){
			equality = true;
		}
		x = rest(x);
		y = rest(y);
	}
	
	return equality;
}

private static int count(Cons lst, int count){
	if(lst == null){
		return count;
	}
	else return count(rest(lst), count + 1);
}

public static Cons setDifference (Cons x, Cons y) {
	return mergediff(llmergesort(x), llmergesort(y));
}

    // following is a helper function for setDifference
public static Cons mergediff (Cons x, Cons y) {
	if ( x == null || y == null ) {
		return null;
	}
	else if ( first(x).equals(first(y)) ){
	   return mergediff(rest(x), rest(y));
	}
	else if ( ((Comparable) first(x)).compareTo(first(y)) < 0){
		return cons(first(x), mergediff(rest(x), y));
	}
	else return mergediff(x, rest(y));
}

private static boolean throughout(Object item, Cons lst){
	
	while(first(lst) != null){
		if(first(lst).equals(item)){
			return true;
		}
		lst = rest(lst);
	}
	return false;
}

public static Cons bank(Cons accounts, Cons updates) {
	Cons completeUpdates = bankHelper(accounts, llmergesort(updates), list());
	return llmergesort(completeUpdates);
}

private static Cons bankHelper(Cons accounts, Cons updates, Cons result){
	Cons tempUpdates = updates;
	Cons backtrack = updates;
	Cons tempResults = result;
	boolean equal = false;
	int count = 0;
	int updatedAmount = 0;
	if( accounts == null){
		boolean found = false;
		while(backtrack != null){
			Account item = ((Account)first(backtrack));
			found = false;
			while(tempResults != null){
				if(item.name().equals(((Account)first(tempResults)).name())){
					System.out.println("FOUND " + ((Account)first(backtrack)).name() + " in tempResults" );
					found = true;
				}
				tempResults = rest(tempResults);
			}
			
			tempResults = result;
			
			if(!found){
				System.out.println("ADDING BECAUSE NOT FOUND IN RESULTS");
				result = cons(first(backtrack), result);
			}
			backtrack = rest(backtrack);

		}
		return result;
	}
	else{
		Account newAccount = null;
		updatedAmount = ((Account)first(accounts)).amount();
		while( tempUpdates != null){
			//System.out.println(((Account)first(accounts)).name() + " compare to update " + ((Account)first(temp)).name());
			if(((Account)first(tempUpdates)).name().equals(((Account)first(accounts)).name())){//matching names
				count++;
				
				updatedAmount += ((Account)first(tempUpdates)).amount();
				if(updatedAmount < 0){
					System.out.println("This transaction for " 
							+ ((Account)first(tempUpdates)).name() 
							+ " has caused an overdraft. A penalty of $30 will be charged.");
					updatedAmount -= 30;
				}
				equal = true;
			}
		
			tempUpdates = rest(tempUpdates);
		}
		
		if(count <= 0){//in account but not updates
			newAccount = new Account(((Account)first(accounts)).name(), ((Account)first(accounts)).amount());
		}
		
		if(equal){			
			newAccount = new Account(((Account)first(accounts)).name(), updatedAmount);
		}
		
		result = cons(newAccount, result);
		return bankHelper(rest(accounts), updates, result);
	}
	
}

public static String [] mergearr(String [] x, String [] y) {
	String [] result = new String [x.length + y.length];
	return mergearrHelper(x, y, 0, 0, result, 0);
}

private static String [] mergearrHelper(String [] x, String [] y, int indexX, int indexY, String [] result, int indexR){
	if(indexX >= x.length && indexY >= y.length){
		return result;
	}	
	else if ( indexX >= x.length) {
		result[indexR] = y[indexY];
		return mergearrHelper(x, y, indexX, indexY + 1, result, indexR + 1);
	}
	else if( indexY >= y.length){
		result[indexR] = x[indexX];
		return mergearrHelper(x, y, indexX + 1, indexY, result, indexR + 1);
	}
	else if ( x[indexX].equals(y[indexY]) ){
		result[indexR] = x[indexX];
		result[indexR + 1] = y[indexY];
		return mergearrHelper(x, y, indexX + 1, indexY + 1, result, indexR + 2);    
	}
	else if ( x[indexX].compareTo(y[indexY]) < 0){
		result[indexR] = x[indexX];
		return mergearrHelper(x, y, indexX + 1, indexY, result, indexR + 1);    
	}
	else {
		result[indexR] = y[indexY];
		return mergearrHelper(x, y, indexX, indexY + 1, result, indexR + 1); 
	}
}

private static Cons nreverse(Cons lst){
    Cons revLst = list();
    return nreverseHelper(lst, revLst);
}
private static Cons nreverseHelper(Cons lst, Cons revLst){
    if(lst == null){
      return revLst;
    }
    else{
      return nreverseHelper(rest(lst), cons(first(lst), revLst));
    }
}

public static boolean markup(Cons text) {
	Cons openTags = open(text, list());
	Cons closedTags = closed(text, list());
	closedTags = nreverse(closedTags);
	int count = markupHelper(openTags, closedTags, 1);
	if(count <= 0){
		return true;
	}
	else{
		System.out.println("Tag error at pos " + count);
		return false;
	}
}

private static int markupHelper(Cons open, Cons closed, int countPos){
	if(rest(open) == null && rest(closed) == null){
		return countPos;
	}
	else if(first(open).equals(first(closed))){
		return markupHelper(rest(open), rest(closed), countPos + 1); 
	}
	else return countPos;
}

public static Cons open(Cons text, Cons stack){
    if(text == null) {
    	return stack;
    }
    else if(((String)first(text)).contains("<") && ((String)first(text)).contains(">") && !((String)first(text)).contains("/")){
    	String inside = ((String)first(text)).substring(1,((String)first(text)).length()-1);
    	return open(rest(text),cons(inside , stack));
    }   
    else return open(rest(text), stack);
 }
 public static Cons closed(Cons text, Cons stack){
    if(text == null) {
    	return stack;
    }
    else if(((String)first(text)).contains("<") && ((String)first(text)).contains(">") && ((String)first(text)).contains("/")){
    	String inside = ((String)first(text)).substring(2,((String)first(text)).length()-1);
    	return closed(rest(text),cons(inside, stack));
    }
    else return closed(rest(text), stack);
 }

    // ****** your code ends here ******

    public static void main( String[] args )
      { 
        Cons set1 = list("d", "b", "c", "a");
        Cons set2 = list("f", "d", "b", "g", "h");
        Cons set5 = list("d", "c", "b", "a");
        Cons set6 = list("a", "b", "c", "d");
        System.out.println("set1 = " + Cons.toString(set1));
        System.out.println("set2 = " + Cons.toString(set2));
        System.out.println("isEqual is " + isEqual(set1, set2));
        System.out.println("union = " + Cons.toString(union(set1, set2)));
        Cons set3 = list("d", "b", "c", "a");
        Cons set4 = list("f", "d", "b", "g", "h");
        System.out.println("set3 = " + Cons.toString(set3));
        System.out.println("set4 = " + Cons.toString(set4));
        System.out.println("difference = " +
                           Cons.toString(setDifference(set3, set4)));

        Cons accounts = list(
               new Account("Arbiter", new Integer(498)),
               new Account("Flintstone", new Integer(102)),
               new Account("Foonly", new Integer(123)),
               new Account("Kenobi", new Integer(373)),
               new Account("Rubble", new Integer(514)),
               new Account("Tirebiter", new Integer(752)),
               new Account("Vader", new Integer(1024)) );

        Cons updates = list(
               new Account("Foonly", new Integer(100)),
               new Account("Flintstone", new Integer(-10)),
               new Account("Arbiter", new Integer(-600)),
               new Account("Garble", new Integer(-100)),
               new Account("Rabble", new Integer(100)),
               new Account("Flintstone", new Integer(-20)),
               new Account("Foonly", new Integer(10)),
               new Account("Tirebiter", new Integer(-200)),
               new Account("Flintstone", new Integer(10)),
               new Account("Flintstone", new Integer(-120)) );
        System.out.println("accounts = " + accounts.toString());
        //System.out.println("updates = " + updates.toString());
        //System.out.println("Sorted Updates = " + llmergesort(updates).toString());
        Cons newaccounts = bank(accounts, updates);
        System.out.println("result = " + newaccounts.toString());

        String[] arra = {"a", "big", "dog", "hippo"};
        String[] arrb = {"canary", "cat", "fox", "turtle"};
        String[] resarr = mergearr(arra, arrb);
        for ( int i = 0; i < resarr.length; i++ ){
            System.out.println(resarr[i]);

        }

        Cons xmla = list( "<TT>", "foo", "</TT>");
        Cons xmlb = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</TR>", "</TABLE>" );
        Cons xmlc = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</WHAT>", "</TABLE>" );
        Cons xmld = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "", "</TR>",
                          "</TABLE>", "</NOW>" );
        Cons xmle = list( "<THIS>", "<CANT>", "<BE>", "foo", "<RIGHT>" );
        Cons xmlf = list( "<CATALOG>",
                          "<CD>",
                          "<TITLE>", "Empire", "Burlesque", "</TITLE>",
                          "<ARTIST>", "Bob", "Dylan", "</ARTIST>",
                          "<COUNTRY>", "USA", "</COUNTRY>",
                          "<COMPANY>", "Columbia", "</COMPANY>",
                          "<PRICE>", "10.90", "</PRICE>",
                          "<YEAR>", "1985", "</YEAR>",
                          "</CD>",
                          "<CD>",
                          "<TITLE>", "Hide", "your", "heart", "</TITLE>",
                          "<ARTIST>", "Bonnie", "Tyler", "</ARTIST>",
                          "<COUNTRY>", "UK", "</COUNTRY>",
                          "<COMPANY>", "CBS", "Records", "</COMPANY>",
                          "<PRICE>", "9.90", "</PRICE>",
                          "<YEAR>", "1988", "</YEAR>",
                          "</CD>", "</CATALOG>");
        System.out.println("xmla = " + xmla.toString());
        System.out.println("result = " + markup(xmla));
        System.out.println("xmlb = " + xmlb.toString());
        System.out.println("result = " + markup(xmlb));
        System.out.println("xmlc = " + xmlc.toString());
        System.out.println("result = " + markup(xmlc));
        System.out.println("xmld = " + xmld.toString());
        System.out.println("result = " + markup(xmld));
        System.out.println("xmle = " + xmle.toString());
        System.out.println("result = " + markup(xmle));
        System.out.println("xmlf = " + xmlf.toString());
        System.out.println("result = " + markup(xmlf));
        
      }

}