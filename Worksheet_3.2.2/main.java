public class main {
    public static void main(String[] args) {
        System.out.println("Guitar Performance System");

        AcousticGuitar acoustic = new AcousticGuitar("Wonderwall", 4);
        ElectricGuitar electric = new ElectricGuitar("Iron Man", 5);

        GuitarPerformance[] performances = {acoustic, electric};
        
        System.out.println("Dynamic Polymorphism");
        for (GuitarPerformance g : performances) {
            g.displayInfo();
            g.play();
            System.out.println();
        }

        System.out.println("Static Polymorphism");
        acoustic.play(120);
    }
}