package ua.netcrackerteam.GUI;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ua.netcrackerteam.controller.HRPage;
import ua.netcrackerteam.controller.bean.StudentsMarks;
import ua.netcrackerteam.util.xls.entity.XlsUserInfo;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Miralissa
 * Date: 24.08.13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class MarksLayout extends VerticalLayout {
    private BeanItem<XlsUserInfo> studentsMarksBeanItem;
    private MarksMode marksMode;
    private HorizontalLayout dropDownsLayout = new HorizontalLayout();
    private HRPage controller = new HRPage();
    private Button saveEditButton;
    private static String SAVE = "Сохранить";
    private static String EDIT = "Редактировать";
    private XlsUserInfo studentsMarks;

    public MarksLayout(XlsUserInfo studentsMarks, MarksMode mode) {
        this.marksMode = mode;
        this.studentsMarks = studentsMarks;
        studentsMarksBeanItem = new BeanItem<XlsUserInfo>(studentsMarks);
        setSpacing(true);
        setMargin(true);
        setWidth(100, UNITS_PERCENTAGE);
        addHeader();
        dropDownsLayout.setSpacing(true);
        addComponent(dropDownsLayout);
        addEnrollmentDropDown();
        addCommandWorkDropDown();
        if (mode.equals(marksMode.INTERVIEWER)) {
            addJavaKnowledgeField();
            addSQLKnowledgeField();
        }
        addCommentField();
        addSaveButton();
    }

    private void addSaveButton() {
        saveEditButton = new Button(SAVE);
        saveEditButton.setWidth(150,UNITS_PIXELS);
        saveEditButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(saveEditButton.getCaption().equals(SAVE))  {
                    saveMarks();
                } else {
                    setReadOnly(false);
                }
            }
        });
        addComponent(saveEditButton);
    }

    private void saveMarks() {
       if(isAllValid()) {
           controller.saveHRMarks(studentsMarks);
           setReadOnly(true);
       } else {
           getWindow().showNotification("Заполните все поля!", Window.Notification.TYPE_TRAY_NOTIFICATION);
       }
    }

    private boolean isAllValid() {
        boolean isValid = true;
        Iterator<Component> i = getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if(c instanceof TextArea) {
                TextArea textArea = (TextArea) c;
                isValid &= textArea.isValid();
            }
        }
        i = dropDownsLayout.getComponentIterator();
        while (i.hasNext()) {
            ComboBox comboBox = (ComboBox) i.next();
            isValid &= comboBox.isValid();
        }
        return isValid;
    }

    private void addHeader() {
        String interviewerName;
        Label header = new Label();
        StringBuilder headerCaption = new StringBuilder();
        headerCaption.append("Оценка ");
        if (marksMode.equals(marksMode.HR)) {
            headerCaption.append("HR");
            interviewerName = studentsMarks.getHr1();
        } else {
            headerCaption.append("технического интервьюера");
            interviewerName = studentsMarks.getHr2();
        }
        if (interviewerName != "") {
            headerCaption.append(" (" + interviewerName + ")");
        }
        headerCaption.append(":");
        header.setCaption(headerCaption.toString());
        addComponent(header);
    }

    private void addCommentField() {
        TextArea commentField = new TextArea();
        commentField.setWidth(100, UNITS_PERCENTAGE);
        commentField.setRows(3);
        commentField.setRequired(true);
        if (marksMode.equals(MarksMode.HR)) {
            commentField.setCaption("Общие впечатления");
        } else {
            commentField.setCaption("Другие впечатления");
        }
        if(marksMode.equals(MarksMode.HR)) {
            commentField.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("comment1"));
        }  else {
            commentField.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("comment2"));
        }
        addComponent(commentField);
    }

    private void addSQLKnowledgeField() {
        TextArea sqlField = new TextArea("Знание SQL");
        sqlField.setRows(3);
        sqlField.setRequired(true);
        sqlField.setWidth(100, UNITS_PERCENTAGE);
        sqlField.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("sqlKnowledge"));
        addComponent(sqlField);
    }

    private void addJavaKnowledgeField() {
        TextArea javaField = new TextArea("Знания Java");
        javaField.setRows(3);
        javaField.setRequired(true);
        javaField.setWidth(100, UNITS_PERCENTAGE);
        javaField.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("javaKnowledge"));
        addComponent(javaField);
    }

    private void addCommandWorkDropDown() {
        ComboBox commandWork = new ComboBox("Готов ли к работе в команде");
        commandWork.setWidth(240, UNITS_PIXELS);
        commandWork.setRequired(true);
        commandWork.setNullSelectionAllowed(false);
        if(marksMode.equals(MarksMode.HR)) {
            commandWork.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("work_in_team1"));
        } else {
            commandWork.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("work_in_team2"));
        }
        Integer yesItem = 1;
        Integer noItem = 0;
        commandWork.addItem(yesItem);
        commandWork.addItem(noItem);
        commandWork.setItemCaption(yesItem, "Да");
        commandWork.setItemCaption(noItem, "Нет");

        dropDownsLayout.addComponent(commandWork);
    }

    private void addEnrollmentDropDown() {
        ComboBox enrollmentScores = new ComboBox("Зачисление");
        enrollmentScores.setWidth(240, UNITS_PIXELS);
        enrollmentScores.setRequired(true);
        if(marksMode.equals(MarksMode.HR)) {
            enrollmentScores.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("result1"));
        } else {
            enrollmentScores.setPropertyDataSource((Property) studentsMarksBeanItem.getItemProperty("result2"));
        }
        enrollmentScores.setNullSelectionAllowed(false);
        List<String> scoresList = controller.getEnrollmentScores();
        BeanItemContainer<String> objects = new BeanItemContainer<String>(String.class, scoresList);
        enrollmentScores.setContainerDataSource(objects);
        dropDownsLayout.addComponent(enrollmentScores);
    }

    public void setReadOnly(boolean isReadOnly) {
        Iterator<Component> i = getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if(c instanceof Button) {

            } else {
                c.setReadOnly(isReadOnly);
            }
        }
        i = dropDownsLayout.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(isReadOnly);
        }
        if(isReadOnly) {
            saveEditButton.setCaption(EDIT);
        } else {
            saveEditButton.setCaption(SAVE);
        }

    }

    public void setEditable(boolean isEditable) {
        saveEditButton.setVisible(isEditable);
    }

    public static enum MarksMode {
        HR, INTERVIEWER
    }

}
