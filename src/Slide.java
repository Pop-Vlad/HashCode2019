import java.util.List;
import java.util.SortedSet;

public class Slide {

    int[] tags;
    int first;
    int second;


    public Slide(int first, int[] tags) {
        this.first = first;
        this.second = -1;
        this.tags = tags;
    }

    public Slide(int first, int second, int[] tags) {
        this.first = first;
        this.second = second;
        this.tags = tags;
    }

    public Slide() {
        this.first = -1;
        this.second = -1;
    }

    public int[] getTags() {
        return tags;
    }

    public int getNoTags() {
        return tags.length;
    }

    @Override
    public String toString(){
        String s = Integer.toString(this.first);
        if(second != -1)
            s += " " + second;
        return s;
    }

}
