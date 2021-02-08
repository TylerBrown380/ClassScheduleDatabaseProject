package edu.boisestate.cs410.gradebook;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class GradeBookShell {
    private final Connection db;

    public GradeBookShell(Connection cxn) {
        db = cxn;
    }

    /**
     * Creates a new class. TYLER
     */
    @Command
    public void newClass(String desc, int courseNum, int sectionNum, String term){
            try(PreparedStatement stmt = db.prepareStatement("INSERT INTO class(description, course_num, section_num, term) " +
                    "VALUES(?,?,?,?)")){
                stmt.setString(1, desc);
                stmt.setInt(2, courseNum);
                stmt.setInt(3, sectionNum);
                stmt.setString(4, term);
                int i = stmt.executeUpdate();
                System.out.println(i + "New Class Inserted");
            }catch (SQLException e){
                e.getStackTrace();
            }
    }


    /**
     * Lists classes with the number of students in each. TYLER
     */
    @Command
    public void listClass(){
        try(PreparedStatement stmt = db.prepareStatement(
                "SELECT class.description, COUNT(students_classes.student_id) " +
                        "FROM class INNER JOIN students_classes " +
                        "ON class.class_id = students_classes.class_id " +
                        "GROUP BY class.class_id"
        )){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String desc = rs.getString("description");
                int i = rs.getInt("count");
                System.out.println("Class: " + desc +", Number of Students: " + i);
            }
        }catch (SQLException e){
            e.getStackTrace();
        }
    }

    /**
     * Selects classes. TYLER
     *
     * select-class CS4100:
     *      selects the only section of CS410 in the most recent
     *      term, if there is only 1 section, fails if there are multiple sections
     *
     * select-class CS410 Sp20:
     *      selects the only section of CS410 in Spring 2020,
     *      if there are multiple sections, it fails.
     *
     * select-class CS410 Sp20 1:
     *      selects a specific section.
     * @param section
     * @param semester
     * @param sectionNumber
     */
    @Command
    public void selectClass(String section, String semester, Integer sectionNumber){}

    //FOLLOWING COMMANDS ASSUME "CURRENTLY ACTIVE CLASS" STATUS
    //Currently active class: works like a directory in a Unix command line,
    //and can be tracked by using a field in the shell class to track it.

    /**
     * Shows the currently-active class. - SANDRA - NEEDS TESTING,
     * ALSO HOW THE HECK DO WE TRACK CURRENT DIRECTORY?
     */
    @Command
    public void showClass(){
        String query = "SELECT description, course_num, section_num, term" +
                "FROM class";
        try(Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query)){
            System.out.println("Class:%n");
            while(rs.next()){
                System.out.format("%d, %s%n",
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Lists the categories with their weights. - SANDRA
     * NEEDS TESTING - NEEDS TO KEEP TRACK OF CURRENT CLASS
     */
    @Command
    public void showCategories(){
        String query = "SELECT weight, name" +
                "FROM categories";
        try(Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query)){
            System.out.println("Categories:%n");
            while(rs.next()){
                System.out.format("%d, %s%n",
                        rs.getDouble("weight"),
                        rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds new categories.
     *
     * For example:
     * add-category "Name" weight
     * @param name
     * @param weight
     */
    @Command
    public void addCategory(String name, Double weight){}

    /**
     * Lists the items with their point values, - SANDRA
     * grouped by category.
     * NEEDS TESTING, NEEDS TO TRACK CURRENT DIRECTORY
     */
    @Command
    public void showItems(){
        String query = "SELECT description, point_val, name" +
                "FROM categories" +
                "JOIN items" +
                "ON items.categories_id = categories.categories_id" +
                "GROUP BY name";
        try(PreparedStatement stmt = db.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Items:%n");
            while(rs.next()){
                System.out.format("%d, %s%n",
                        rs.getString("description"),
                        rs.getInt("point_val"),
                        rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new item.
     *
     * For example:
     * add-item name "Category" "Description" points
     * @param category
     * @param description
     */
    @Command
    public void addItem(String category, String description){}

    /**
     * Adds and enrolls a student in the current class.
     *
     * For example:
     *  add-student username studentid "Last, First":
     *      If the student already exists, then they are enrolled.
     *      If the name does not match the stored name, then the name is update
     *      with a warning to user about the change of name.
     *  add-student username:
     *      adds already existing student, if not found print error
     * @param username
     * @param studentid
     */
    @Command
    public void addStudent(String username, Integer studentid){}

    /**
     * Shows all the student in the current class. - SANDRA
     *
     * For example:
     * show-students:
     *      shows all students in the current class.
     *
     * show-students EKS:
     *      shows all students with "EKS" in their name or username,
     *      case-sensitive
     */
    @Command
    public void showStudents(String pattern){}

    /**
     * For example: - SANDRA
     * grade itemname username 20:
     *      assigns a grade of 20 for student with username for itemname
     *      If grade already exists, replace existing grade
     *      If the number of points exceeds the number of configured points for the item,
     *      then print a warning with the points configured
     *
     * @param itemname
     * @param username
     * @param grade
     */
    @Command
    public void grade(String itemname, String username, Integer grade){}

    /**
     * For example: student-grades username:
     *      shows the current grade for student username,
     *      all items are grouped by category,
     *      with the student's grade,
     *      along with subtotals for each category
     *      and and overall class grade.
     *
     * @param username
     */
    @Command
    public void studentGrades(String username){}

    /**
     * Shows the current class's gradebook.
     * - students (username, student ID, and name)
     * - total grades for each student in the class
     */
    @Command
    public void gradebook(){}












    public static void main(String[] args) throws IOException, SQLException {
        // First (and only) command line argument: database URL
        String dbUrl = args[0];
        try (Connection cxn = DriverManager.getConnection("jdbc:" + dbUrl)) {
            GradeBookShell shell = new GradeBookShell(cxn);
            ShellFactory.createConsoleShell("gradebook", "", shell)
                    .commandLoop();
        }
    }
}