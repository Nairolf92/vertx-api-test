/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.example.web.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleREST extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SimpleREST.class);
  }

  private Map<String, JsonObject> teachers = new HashMap<>();
  private Map<String, JsonObject> parents = new HashMap<>();
  private Map<String, JsonObject> messages = new HashMap<>();

  @Override
  public void start() {

    setUpInitialData();

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
//    TEACHER
    router.get("/teachers/:teacherID/messages").handler(this::handleGetTeacherMessages);
    router.get("/teachers/:teacherID/messages/:messageID").handler(this::handleGetTeacherMessage);
    router.put("/teachers/:teacherID/messages").handler(this::handleAddTeacherMessage);
    router.get("/teachers").handler(this::handleListTeachers);
//    PARENT

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  private void handleGetTeacherMessages(RoutingContext routingContext) {
    String teacherID = routingContext.request().getParam("teacherID");
    HttpServerResponse response = routingContext.response();
    if (teacherID == null) {
      sendError(400, response);
    } else {
      JsonArray arr = new JsonArray();
      for (Map.Entry<String, JsonObject> entry : messages.entrySet()) {
        JsonObject jsonEntry = entry.getValue();
        System.out.println(jsonEntry);
        System.out.println(jsonEntry.getString("teacherID"));
        System.out.println(teacherID);
        if (jsonEntry.getString("teacherID").toString().equals(teacherID)) {
          System.out.println("inside if");
          arr.add(jsonEntry);
        }
      }
      routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
    }
  }

  private void handleGetTeacherMessage(RoutingContext routingContext) {
    String teacherID = routingContext.request().getParam("teacherID");
    String messageID = routingContext.request().getParam("messageID");
    HttpServerResponse response = routingContext.response();
    if (teacherID == null || messageID == null) {
      sendError(400, response);
    } else {
      JsonObject message = messages.get(messageID);
      if (message == null) {
        sendError(404, response);
      } else {
        response.putHeader("content-type", "application/json").end(message.encodePrettily());
      }
    }
  }

  private void handleAddTeacherMessage(RoutingContext routingContext) {
    String teacherID = routingContext.request().getParam("teacherID");
    HttpServerResponse response = routingContext.response();
    if (teacherID == null) {
      sendError(400, response);
    } else {
      JsonObject teacher = routingContext.getBodyAsJson();
      if (teacher == null) {
        sendError(400, response);
      } else {
        teacher.put(teacherID, messages);
        response.end();
      }
    }
  }

  private void handleListTeachers(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    teachers.forEach((k, v) -> arr.add(v));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData() {
    addTeacher(new JsonObject().put("id", "9").put("name", "Florian Kelnerowski"));
    addTeacher(new JsonObject().put("id", "8").put("name", "Charlie Brown"));
    addMessage(new JsonObject().put("id", "1").put("teacherID", "9"));
    addMessage(new JsonObject().put("id", "2").put("teacherID", "9"));
    addMessage(new JsonObject().put("id", "3").put("teacherID", "8"));
  }

  private void addTeacher(JsonObject teacher) {
    teachers.put(teacher.getString("id"), teacher);
  }

  private void addMessage(JsonObject message) {
    messages.put(message.getString("id"), message);
  }
}
