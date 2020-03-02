import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static final boolean H = true;
    public static final boolean V = false;
    public static int batchSize = -1;
    public static String fileName = "b.txt";

    public static int setDifference(int[] s1, int[] s2){
        int n = 0;
        int n1 = s1.length;
        int n2 = s2.length;
        int i1 = 0;
        int i2 = 0;
        while(i1 < n1 && i2 < n2){
            if(s1[i1] == s2[i2]){
                n++;
                i1++;
                i2++;
            }
            else if(s1[i1] > s2[i2])
                i2++;
            else
                i1++;
        }
        return n;
    }

    public static int interestFactor(Slide s1, Slide s2){
        int n1 = s1.getTags().length;
        int n2 = s1.getTags().length;
        int n3 = setDifference(s1.getTags(), s2.getTags());
        n1 -= n3;
        n2 -= n3;
        if(n1 < n2)
            return Math.min(n1, n2);
        else
            return Math.min(n2, n3);
    }

    public static Slide findBestMatch(Slide s, List<Slide> slides){
        return Collections.max(slides, Comparator.comparingInt(e -> interestFactor(s, e)));
    }

    public static SlideGroup findBestMatch(SlideGroup s, List<SlideGroup> slides){
        Slide lastSlide = s.getLastSlide();
        return Collections.max(slides, Comparator.comparingInt(e -> interestFactor(lastSlide, e.getFirstSlide())));
    }

    public static int score(List<Slide> slides){
        int s = 0;
        for(int i=0; i<slides.size()-1; i++) {
            s += interestFactor(slides.get(i), slides.get(i + 1));
        }
        return s;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        long startTime = System.nanoTime();
        FileInputStream input = new FileInputStream(new File(fileName));
        Scanner scanner = new Scanner(input);
        int N = scanner.nextInt();
        if(batchSize == -1)
            batchSize = N;
        List<Integer>[] photos = new List[N];
        boolean[] types = new boolean[N];

        Map<String, Integer> tagsMap = new HashMap<String, Integer>();

        for(int i=0; i<N; i++){
            String type = scanner.next();
            String noTags = scanner.next();
            String line = scanner.nextLine();
            List<String> tags = Arrays.asList(line.strip().split(" "));
            for(String tag : tags)
                if(!tagsMap.containsKey(tag))
                    tagsMap.put(tag, tagsMap.size());
            types[i] = type.equals("H");
            photos[i] = tags.stream()
                    .map(tagsMap::get)
                    .collect(Collectors.toList());
        }

        LinkedList<Slide> slides = new LinkedList<>();
        for(int i=0; i<N; i++){
            if(types[i]) {
                List l = new ArrayList<>(photos[i]);
                l.sort(Comparator.comparingInt(e -> (int) e));
                int[] ret = new int[l.size()];
                for (int k=0; k < l.size(); k++)
                {
                    ret[k] = (Integer) l.get(k);
                }
                slides.add(new Slide(i, ret));
            }
        }
        slides.sort((slide1, slide2) -> slide1.getNoTags() - slide2.getNoTags());

        LinkedList<SlideGroup> groups = new LinkedList<>();

        int k = 0;
        while(slides.size() > 0){
            k++;
            if(k%100 == 0)
                System.out.println("Iteration: " + k);
            Slide first = slides.removeFirst();
            Slide second = findBestMatch(first, slides);
            slides.remove(second);
            SlideGroup g = new SlideGroup(first, second);
            groups.add(g);
        }

        while(groups.size() > 1) {
            LinkedList<SlideGroup> newGroups = new LinkedList<>();
            while (groups.size() > 1) {
                k++;
                if(k%100 == 0)
                    System.out.println("Iteration: " + k);
                SlideGroup s1 = groups.removeFirst();
                SlideGroup s2 = findBestMatch(s1, groups);
                groups.remove(s2);
                newGroups.add(new SlideGroup(s1, s2));
            }
            newGroups.addAll(groups);
            groups = newGroups;
        }

        /*
        int count = slides.size()/batchSize;
        List[] parts = new LinkedList[count];
        Thread[] t = new Thread[count];
        for(int i=0; i<count; i++) {
            parts[i] = new LinkedList<>(slides.subList(i * batchSize, (i + 1) * batchSize));
        }

        for(int i=0; i<count; i++) {
            int idx = i;
            t[i] = new Thread(){
                public void run(){
                    LinkedList<Slide> slideShowPart = new LinkedList<>();
                    slideShowPart.add((Slide) parts[idx].get(0));
                    parts[idx].remove(0);
                    while (parts[idx].size() > 0) {
                        float progress = (batchSize - parts[idx].size()) / (float)batchSize * 100;
                        if(progress - (int) progress == 0)
                            System.out.println("Batch " + idx + ": " + progress * 1 + "%" );
                        Slide s1 = slideShowPart.getFirst();
                        Slide s2 = slideShowPart.getLast();
                        Slide s1m = findBestMatch(s1, parts[idx]);
                        Slide s2m = null;
                        if (true) {
                            slideShowPart.addFirst(s1m);
                            parts[idx].remove(s1m);
                        } else {
                            slideShowPart.addLast(s2m);
                            parts[idx].remove(s2m);
                        }
                    }
                    slideShow.addAll(slideShowPart);
                }
            };
            t[i].start();
        }

        for(int i=0; i<count; i++) {
            t[i].join();
        }
        */

        SlideGroup slideShow = groups.getFirst();
        int count = slideShow.count();
        List<Slide> l = slideShow.getAll();
        FileWriter output = new FileWriter(new File(fileName.substring(0, 1) + "_temp.txt"));
        BufferedWriter bw = new BufferedWriter(output);
        bw.write(Integer.toString(count));
        bw.write("\n");
        for(int i=0; i<l.size(); i++){
            bw.write(((l.get(i))).toString());
            bw.write("\n");
        }
        bw.close();

        System.out.println("\n");
        //System.out.println(slideShow);
        System.out.println(score(l));
        long endTime = System.nanoTime();
        System.out.println("\n");
        System.out.println((endTime-startTime)/1000000 + "ms");

    }

}
