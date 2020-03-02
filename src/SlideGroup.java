import java.util.LinkedList;
import java.util.List;

public class SlideGroup extends Slide {

    private Slide first;
    private Slide second;

    public SlideGroup(Slide first, Slide second){
        this.first = first;
        this.second = second;
    }

    public Slide getFirstSlide(){
        if(first instanceof SlideGroup)
            return ((SlideGroup)first).getFirstSlide();
        else
            return this.first;
    }

    public Slide getLastSlide(){
        if(second instanceof SlideGroup)
            return ((SlideGroup)second).getLastSlide();
        else
            return this.second;
    }

    public int count(){
        int n1 = 1;
        int n2 = 1;
        if(first instanceof SlideGroup)
            n1 = ((SlideGroup)first).count();
        if(second instanceof SlideGroup)
            n2 = ((SlideGroup)second).count();
        return n1+n2;
    }

    public List<Slide> getAll(){
        List<Slide> l1 = new LinkedList<>();
        l1.add(this.first);
        List<Slide> l2 = new LinkedList<>();
        l2.add(this.second);
        if(first instanceof SlideGroup)
            l1 = ((SlideGroup)first).getAll();
        if(second instanceof SlideGroup)
            l2 = ((SlideGroup)second).getAll();
        l1.addAll(l2);
        return l1;
    }

}
