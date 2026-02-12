public class Factorial {
    private int calculate(int y){
        if(y!=1){
            return y*calculate(y-1);
        }
        else{
            return 1;
        }
    }
    public void run(int x){
        System.out.println("Factorial for "+x+" is: "+calculate(x));
    }
}
