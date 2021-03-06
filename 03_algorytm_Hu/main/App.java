import java.util.ArrayList;

public class App
{
  public static TaskManager tm = new TaskManager();
  public static MachineManager mm = new MachineManager();
  public static Graphic graphic = new Graphic();
  public static DataFromFile dff = new DataFromFile();

  public static void clearScreen()
  {
    System.out.print("\033[H\033[2J");
  }

  public static void displayHeader()
  {
    clearScreen();
    info("╔═╗╦  ╔═╗╔═╗╦═╗╦ ╦╔╦╗╔╦╗  ╦ ╦╦ ╦");
    info("╠═╣║  ║ ╦║ ║╠╦╝╚╦╝ ║ ║║║  ╠═╣║ ║");
    info("╩ ╩╩═╝╚═╝╚═╝╩╚═ ╩  ╩ ╩ ╩  ╩ ╩╚═╝");
  }

  public static void info(String text)
  {
    System.out.println(text);
  }

  public static void newLines(int linesNumber)
  {
    for (int i = 0; i < linesNumber; i++)
    {
      info("");
    }
  }

  public static void closeApp(String text)
  {
    info(text);
    System.exit(0);
  }

  public static void main(String args[])
  {
    displayHeader();
    newLines(1);

    String filename = "dane.txt";     // plik z danymi do grafu

    dff.prepareData(filename);
    graphic.makeGraphic("graph");
    tm.setLevels();
    tm.displayAllTasks();
    newLines(1);
    mm.prepareSchedules();
    mm.displayAllMachines();
    graphic.makeGraphic("schedule");
    newLines(1);
  }
}
