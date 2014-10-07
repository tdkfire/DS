// libtest.java      GSN    03 Oct 08; 21 Feb 12; 26 Dec 13
// 

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

@SuppressWarnings("unchecked")
public class libtest {

    // ****** your code starts here ******


public static Integer sumlist(LinkedList<Integer> lst) {
	ListIterator<Integer> it = lst.listIterator();
	Integer sum = Integer.valueOf(0);
	while(it.hasNext()){
		sum+=it.next();
	}
	return sum;
}

public static Integer sumarrlist(ArrayList<Integer> lst) {
	ListIterator<Integer> it = lst.listIterator();
	Integer sum = Integer.valueOf(0);
	while(it.hasNext()){
		sum+=it.next();
	}
	return sum;
}

public static LinkedList<Object> subset (Predicate p,
                                          LinkedList<Object> lst) {
	LinkedList<Object> result = new LinkedList<Object>();
	ListIterator it = lst.listIterator();
	while(it.hasNext()){
		 Object item = it.next();
		if(p.pred(item)){
			result.add(item);
		}
	}
	return result;
}

public static LinkedList<Object> dsubset (Predicate p,
                                           LinkedList<Object> lst) {
	ListIterator it = lst.listIterator();
	while(it.hasNext()){
		 Object item = it.next();
		if(p.pred(item)){
			it.remove();
		}
	}
	return lst;
}

public static Object some (Predicate p, LinkedList<Object> lst) {
	ListIterator<Object> it = lst.listIterator();
	while(it.hasNext()){
		Object item = it.next();
		if(p.pred(item)){
			return item;
		}
	}
	return null;
}

public static LinkedList<Object> mapcar (Functor f, LinkedList<Object> lst) {
	LinkedList<Object> result = new LinkedList<Object>();
	ListIterator<Object> it = lst.listIterator();
	while(it.hasNext()){
		result.add(f.fn(it.next()));
	}
	return result;
}

public static LinkedList<Object> merge (LinkedList<Object> lsta,
                                        LinkedList<Object> lstb) {

	LinkedList<Object> result = new LinkedList<Object>();
	
	ListIterator<Object> itA = lsta.listIterator();
	ListIterator<Object> itB = lstb.listIterator();
	
	while(itA.hasNext() && itB.hasNext()){
		Object item1 = itA.next();
		Object item2 = itB.next();
		if(((Comparable)item1).compareTo(((Comparable)item2)) > 0){
			result.add(item2);
			itA.previous();
		}
		else if(((Comparable)item1).compareTo(((Comparable)item2)) < 0){
			result.add(item1);
			itB.previous();
		}
		else{
			result.add(item1);
			result.add(item2);
		}
	}
	while(itA.hasNext()){//if any left over in lista, put rest in result
		result.add(itA.next());
	}
	while(itB.hasNext()){//if any left over in listb, put rest in result
		result.add(itB.next());
	}
	
	return result;
}

public static LinkedList<Object> sort (LinkedList<Object> lst) {
	
	return sortHelper(lst, new LinkedList<Object>(), new LinkedList<Object>(), new LinkedList<Object>());
}

private static LinkedList<Object> sortHelper(LinkedList<Object> lst, LinkedList<Object> left, LinkedList<Object> right,
										LinkedList<Object> result){
	
	if(lst.size() <= 1){
		return lst;
	}
	else{
		for(int i = 0; i < lst.size()/2; i++){
			left.add(lst.get(i));
		}

		for(int j = lst.size()/2; j < lst.size(); j++){
			right.add(lst.get(j));
		}

		left = sort(left);
		right = sort(right);
		result = merge(left, right);
		return result;	
	}
}

public static LinkedList<Object> intersection (LinkedList<Object> lsta,
                                               LinkedList<Object> lstb) {
	LinkedList<Object> result = new LinkedList<Object>();
	lsta = sort(lsta);
	lstb = sort(lstb);
	ListIterator<Object> ita = lsta.listIterator();
	ListIterator<Object> itb = lstb.listIterator();
	
	
	while(ita.hasNext() && itb.hasNext()){
		Object itema = ita.next();
		Object itemb = itb.next();
		if(((Comparable)itema).compareTo((Comparable)itemb) < 0){
			itb.previous();
		}
		else if(((Comparable)itema).compareTo((Comparable)itemb) > 0){
			ita.previous();
		}
		else{
			result.add(itema);
		}
	}
	return result;
}

public static LinkedList<Object> reverse (LinkedList<Object> lst) {
	ListIterator<Object> it = lst.listIterator();
	LinkedList<Object> result = new LinkedList<Object>();
	
	while(it.hasNext()){
		Object item = it.next();
		result.addFirst(item);
	}
	
	return result;
}

    // ****** your code ends here ******

    public static void main(String args[]) {
        LinkedList<Integer> lst = new LinkedList<Integer>();
        lst.add(new Integer(3));
        lst.add(new Integer(17));
        lst.add(new Integer(2));
        lst.add(new Integer(5));
        System.out.println("lst = " + lst);
        System.out.println("sum = " + sumlist(lst));

        ArrayList<Integer> lstb = new ArrayList<Integer>();
        lstb.add(new Integer(3));
        lstb.add(new Integer(17));
        lstb.add(new Integer(2));
        lstb.add(new Integer(5));
        System.out.println("lstb = " + lstb);
        System.out.println("sum = " + sumarrlist(lstb));

        final Predicate myp = new Predicate()
            { public boolean pred (Object x)
                { return ( (Integer) x > 3); }};

        LinkedList<Object> lstc = new LinkedList<Object>();
        lstc.add(new Integer(3));
        lstc.add(new Integer(17));
        lstc.add(new Integer(2));
        lstc.add(new Integer(5));
        System.out.println("lstc = " + lstc);
        System.out.println("subset = " + subset(myp, lstc));

        System.out.println("lstc     = " + lstc);
        System.out.println("dsubset  = " + dsubset(myp, lstc));
        System.out.println("now lstc = " + lstc);

        LinkedList<Object> lstd = new LinkedList<Object>();
        lstd.add(new Integer(3));
        lstd.add(new Integer(17));
        lstd.add(new Integer(2));
        lstd.add(new Integer(5));
        System.out.println("lstd = " + lstd);
        System.out.println("some = " + some(myp, lstd));

        final Functor myf = new Functor()
            { public Integer fn (Object x)
                { return new Integer( (Integer) x + 2); }};

        System.out.println("mapcar = " + mapcar(myf, lstd));

        LinkedList<Object> lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(6));
        lste.add(new Integer(9));
        lste.add(new Integer(11));
        lste.add(new Integer(23));
        System.out.println("lste = " + lste);
        LinkedList<Object> lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(7));
        System.out.println("lste = " + lste);
        lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        lstf.add(new Integer(10));
        lstf.add(new Integer(12));
        lstf.add(new Integer(17));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        LinkedList<Object> lstg = new LinkedList<Object>();
        lstg.add(new Integer(39));
        lstg.add(new Integer(84));
        lstg.add(new Integer(5));
        lstg.add(new Integer(59));
        lstg.add(new Integer(86));
        lstg.add(new Integer(17));
        System.out.println("lstg = " + lstg);

        System.out.println("intersection(lstd, lstg) = "
                           + intersection(lstd, lstg));
        System.out.println("reverse lste = " + reverse(lste));

        System.out.println("sort(lstg) = " + sort(lstg));

   }
}