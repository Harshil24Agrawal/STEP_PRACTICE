import java.util.Scanner;
class main{
    public static void main(String args[]){
        int a;
        Scanner sc=new Scanner(System.in);
              System.out.println("Hello");
              System.out.println("\nChoose From the following options\n");
              while(true){
              System.out.println("Enter 1 for factorial\nEnter 2 for fibonacci\n");
              a =sc.nextInt();
              if(a==1){
                  System.out.println("Enter the number: ");
                  int b=sc.nextInt();
                  Factorial ft=new Factorial();
                  ft.run(b);
                  break;
              }
        else if(a==2){
                  System.out.println("Enter the number: ");

                  int b=sc.nextInt();
            Fibonacci fb=new Fibonacci();
            fb.run(b);
            break;
        }
        else{
           System.out.println("Invalid Entry try Again!!");
           continue;
        }
              }

    }
}