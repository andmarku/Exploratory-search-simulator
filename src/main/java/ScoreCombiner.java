import java.util.List;

public class ScoreCombiner {
    private double multiplier;
    public ScoreCombiner(double multiplier){
        this.multiplier = multiplier;
    }
    /*
    * Very simple multiplier combiner
    * */
    public double combine(List<Double> scores){
        double totalScore = 0;
        for (double queryScore: scores) {
            totalScore += queryScore;
        }
        return totalScore*multiplier;

    }
}
