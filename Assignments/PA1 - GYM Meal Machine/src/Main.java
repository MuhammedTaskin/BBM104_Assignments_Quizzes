public class Main {

    public static void main(String[] args) {
        String[] contentProduct = FileInput.readFile(args[0], false, false);
        String[] contentPurchase = FileInput.readFile(args[1], false, false);

        FileOutput.writeToFile(args[2], "", false, false);

        Machine machine = new Machine(args[2], contentProduct); //Creates a machine with provided products
        machine.fill();
        machine.writeOutMachine();

        Purchase purchase = new Purchase(args[2], machine.getInventory(), contentPurchase);
        purchase.processPurchase(); //Starts transactions

        machine.writeOutMachine();
    }
}

