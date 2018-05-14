import java.util.ArrayList;
import java.util.Collections;

public class TaskManager
{
  public static ArrayList<Task> tasks = new ArrayList<Task>();

  public static void addTask(Task taskToAdd)
  {
    tasks.add(taskToAdd);
  }

  public static void addConnection(int task1number, int task2number)
  {
    Task task1 = tasks.get(task1number-1);
    Task task2 = tasks.get(task2number-1);

    if (tasks.contains(task1) && tasks.contains(task2))
    {
      task1.getNextTasks().add(task2);
      task2.getPrevTasks().add(task1);
    }
    else
    {
      App.closeApp("Błąd! Któregoś z zadań nie ma na liście zadań.");
    }
  }

  public static void displayConnTaskList(ArrayList<Task> connTaskList)
  {
    if (!connTaskList.isEmpty())
    {
      System.out.print("{");
      int x = 0;
      for (Task task : connTaskList)
      {
        if (x == connTaskList.size()-1)
        {
          System.out.print(task.getTaskNumber());
          break;
        }
        System.out.print(task.getTaskNumber() + ", ");
        x++;
      }
      System.out.print("}");
    }
    else System.out.print("empty");
  }

  public static void displayTasksScheme()
  {
    System.out.println("SCHEME:");
    System.out.println("[Task no. <num>] <duration>, <arrival>, <finish>,");
    System.out.println("        <modFinish>, {<prevTasks>}, {<nextTasks>}");
    System.out.print("\n");
  }

  public static void displayAllTasks()
  {
    displayTasksScheme();
    System.out.println("TASKS:");
    for (Task task : tasks)
    {
      System.out.print("[Task no. " + task.getTaskNumber() + "]");
      System.out.print(" " + task.getDuration() + ",");
      System.out.print(" " + task.getArrivalTime() + ",");
      System.out.print(" " + task.getFinishTime() + ",");
      System.out.print(" " + task.getModFinishTime() + ",");

      System.out.print(" ");
      displayConnTaskList(task.getPrevTasks());
      System.out.print(",");
      System.out.print(" ");
      displayConnTaskList(task.getNextTasks());

      System.out.print("\n");
    }
  }

  public static ArrayList<Integer> assignModTimesForConnectedTasks(
      ArrayList<Task> tasksToAssign, ArrayList<Integer> finishTimes)
  {
    for (Task nextTask : tasksToAssign)
    {
      finishTimes.add(nextTask.getFinishTime());
      finishTimes = assignModTimesForConnectedTasks(nextTask.getNextTasks(), finishTimes);
    }
    return finishTimes;
  }

  public static void assignModifiedFinishTime()
  {
    for (Task task : tasks)
    {
      ArrayList<Integer> finishTimes = new ArrayList<Integer>();
      finishTimes.add(task.getFinishTime());
      finishTimes = assignModTimesForConnectedTasks(task.getNextTasks(), finishTimes);

      Collections.sort(finishTimes);
      task.setModFinishTime(finishTimes.get(0));
    }
  }

  public static Boolean isAllTasksCompleted(ArrayList<Task> tasksToComplete)
  {
    int counter = 0;
    for (Task task : tasksToComplete)
    {
      if (!task.getIsCompleted()) counter++;
    }
    if (counter == 0) return true;
    else return false;
  }

  public static void makeSchedule()
  {
    System.out.println("SCHEDULE:");

    int time = 0;
    int currentTaskId = 0;
    ArrayList<Task> activeTasks = new ArrayList<Task>();
    while (!isAllTasksCompleted(tasks))
    {
      for (Task task : tasks)
      {
        if (task.getArrivalTime() == time)
        {
          task.setIsActive(true);
          activeTasks.add(task);
        }
      }

      int minimalModIndex = 0;
      if (!activeTasks.isEmpty())
      {
        for (Task task1 : activeTasks)
        {
          for (Task task2 : activeTasks)
          {
            if (task1.getModFinishTime() <= task2.getModFinishTime())
            {
              minimalModIndex = task1.getTaskNumber();
            }
            else
            {
              minimalModIndex = task2.getTaskNumber();
            }
          }
        }
        int duration = tasks.get(minimalModIndex-1).getDuration()-1;
        tasks.get(minimalModIndex-1).setDuration(duration);
        System.out.println((time+1) + ": Z" + tasks.get(minimalModIndex-1).getTaskNumber());
      }
      else
      {
        System.out.println((time+1) + ": przerwa");
      }

      for (Task task : tasks)
      {
        if (task.getDuration() == 0)
        {
          task.setIsCompleted(true);
          activeTasks.remove(task);
        }
      }

      time++;
    }
  }
}