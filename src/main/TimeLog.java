package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import resource.GetResource;

/**
 * AutoTimeLog Application for Calculating the time spent on machine.
 * 
 * @author amitkumargi
 * @since 08-Sep-16
 */
public class TimeLog {

  public static void main(String args[]) {
    File file = new File(GetResource.getPath() + "/TimeLog.txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    BufferedWriter writer = null;
    FileWriter f = null;
    try {
      f = new FileWriter(file, true);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    writer = new BufferedWriter(f);
    Timer t = new Timer(writer);
    Date startTime = new Date();
    try {
      writer.write("StartTime-" + startTime + "\n");
      System.out.println("StartTime-" + startTime + "\n");
    } catch (IOException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    t.start();
    Scanner input = new Scanner(System.in);
    int i = -1;
    while (i < 0) {
      i = input.nextInt();
    }
    input.close();
    Date endTime = new Date();
    try {
      writer.write("StopTime-" + endTime + "\n");
      System.out.println("StopTime-" + endTime + "\n");
      writer.close();
      calculateTime(startTime, endTime);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.exit(0);
  }

  private static void calculateTime(Date start, Date end) throws IOException {
    long diffMs = end.getTime() - start.getTime();
    long diffSec = diffMs / 1000;
    long min = diffSec / 60;
    BufferedReader br = null;
    int count = 0;
    try {
      String sCurrentLine;
      br = new BufferedReader(new FileReader(GetResource.getPath() + "/TimeLog.txt"));
      while ((sCurrentLine = br.readLine()) != null) {
        if (sCurrentLine.contains("SkipTime")) {
          count++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
          br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    File file = new File(GetResource.getPath() + "/TimeLog.txt");
    long spentTime = min - (count * 5);
    FileWriter f = new FileWriter(file, true);
    BufferedWriter writer = new BufferedWriter(f);
    System.out.println("SpentTime- " + (spentTime / 60) + "HH:" + (spentTime % 60) + "MM" + "\n");
    writer.write("SpentTime- " + (spentTime / 60) + " HH : " + (spentTime % 60) + " MM" + "\n");
    writer.close();
  }
}

class Timer extends Thread {

  private BufferedWriter writer;

  public Timer(BufferedWriter writer) {
    this.writer = writer;
  }

  public void run() {
    boolean flag = false;
    File file = new File(GetResource.getPath() + "/TimeLog.txt");
    if (!file.exists()) {
      System.out.println("Timelog.txt doesn't exist !!");
      System.exit(0);
    }
    while (true) {
      for (String s : listRunningProcesses()) {
        if (s.equals("LogonUI.exe")) {
          flag = true;
          break;
        }
      }
      if (flag) {
        try {
          writer.write("SkipTime-" + new Date() + "\n");
          System.out.println("SkipTime-" + new Date() + "\n");
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      try {
        sleep(60000 * 5); // wait for 5 mins.
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("Time Log Application Running...");
    }
  }

  public List<String> listRunningProcesses() {
    List<String> processes = new ArrayList<String>();
    try {
      String line;
      Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
        if (!line.trim().equals("")) {
          // keep only the process name
          line = line.substring(1);
          processes.add(line.substring(0, line.indexOf("\"")));
        }

      }
      input.close();
    } catch (Exception err) {
      err.printStackTrace();
    }
    return processes;
  }
}
