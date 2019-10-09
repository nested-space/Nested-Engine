package com.edenrump;

import com.edenrump.graphic.geom.parts.LineSegment2D;
import com.edenrump.math.Vector2f;
import com.edenrump.test.MyModel;

public class Main {

    public static void main(String[] args) {
	// write your code here

        LineSegment2D lineSegment2D = new LineSegment2D(
                new Vector2f(0f, 0),
                new Vector2f(0.03f, 0),
                0.01f,
                true);


        MyModel model = new MyModel();
//        MyObserver observer = new MyObserver(model);


        model.addChangeListener(evt -> {
            System.out.println("Property changed! " + evt.getPropertyName());
        });
        // we change the last name of the person, observer will get notified
        for (MyModel.Person person : model.getPersons()) {
            person.setLastName(person.getLastName() + "1");
        }
        // we change the name of the person, observer will get notified
        for (MyModel.Person person : model.getPersons()) {
            person.setFirstName(person.getFirstName() + "1");
        }

    }
}
