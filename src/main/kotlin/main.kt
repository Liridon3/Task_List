package tasklist

import kotlin.system.exitProcess

fun main() {
    // write your code here
    var taskList = mutableListOf(mutableListOf<String>())
    taskList.clear()
    menu(taskList)
}

fun menu (taskList: MutableList<MutableList<String>>) {
    var taskNumber = 0
    loop@ while (true) {
        println("Input an action (add, print, end):")
        val input = readln()
        if (input == "add") {
            taskListInput(taskList, taskNumber)
            taskNumber += 1
            continue@loop
        } else if (input == "print") {
            if (taskList.isEmpty()) {
                println("No tasks have been input")
            } else {
                printTaskList(taskList)
            }
            continue@loop
        } else if (input == "end") {
            println("Tasklist exiting!")
            exit()
        } else println("The input action is invalid")
    }
}

fun taskListInput (taskList: MutableList<MutableList<String>>, taskNumber: Int) {
    println("Input a new task (enter a blank line to end):")
    var task: String
    var listOfTaskInThisList: MutableList<String> = mutableListOf()
    listOfTaskInThisList.clear()
    loop@while (true) {
        task = readln().trim()
        if (task.isBlank()) {
            if (listOfTaskInThisList.isEmpty()) println("The task is blank")
            break@loop
        } else {
            listOfTaskInThisList.add(task)
        }
    }
    if (listOfTaskInThisList.isNotEmpty()) taskList.add(listOfTaskInThisList)
}

fun blankTask (task: String, taskList: MutableList<MutableList<String>>, taskNumber: Int): Boolean {
    return if (task.isBlank() && taskList[taskNumber].isEmpty()) {
        println("The task is blank")
        true
    } else false
}

fun printTaskList (taskList: MutableList<MutableList<String>>) {
    for (i in 0..taskList.lastIndex) {
        if (i < 9) {
            println("${i+1}  ${taskList[i][0]}")
            for (r in 1..taskList[i].lastIndex) {
                println("   ${taskList[i][r]}")
            }
        } else {
            println("${i+1} ${taskList[i][0]}")
            for (r in 1..taskList[i].lastIndex) {
                println("   ${taskList[i][r]}")
            }
        }
        println()
    }
}

fun exit () {
    exitProcess(0)
}
