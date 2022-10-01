package tasklist

import kotlinx.datetime.*
import java.time.LocalTime
import kotlin.system.exitProcess

enum class DueTag {
    T, I, O
}

fun main() {
    val taskList = mutableListOf(mutableListOf<Any>())
    taskList.clear()
    menu(taskList)
}

fun menu (taskList: MutableList<MutableList<Any>>) {
    var taskNumber = 0
    loop@ while (true) {
        println("Input an action (add, print, edit, delete, end):")
        val input = readln()
        if (input == "add") {
            var priority = priorityInput()
            var date = dateInput()
            var time = timeInput()
            var dueDate = dueDate(date)
            if(taskListInput(taskList, "initial", 0)) continue@loop else
                taskList[taskNumber].add(0, date)
            taskList[taskNumber].add(1, time)
            taskList[taskNumber].add(2, priority)
            taskList[taskNumber].add(3, dueDate)
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
        } else if (input == "edit") {
            editTask(taskList)
            continue@loop
        } else if (input == "delete") {
            deleteTask(taskList)
            continue@loop
        } else println("The input action is invalid")
    }
}

fun dueDate (date: String): DueTag {
    val taskDate = date.toLocalDate()
    val now = Clock.System.now()
    val currentDate = now.toLocalDateTime(TimeZone.of("UTC+0")).date
    var numberOfDays = currentDate.daysUntil(taskDate)
    return when {
        numberOfDays < 0 -> DueTag.O
        numberOfDays == 0 ->  DueTag.T
        else ->  DueTag.I
    }
}

fun priorityInput (): String {
    var priority:String
    loop@while (true) {
        println("Input the task priority (C, H, N, L):")
        priority = readln()
        priority = when (priority) {
            "C" -> "C"
            "H" -> "H"
            "N" -> "N"
            "L" -> "L"
            "c" -> "C"
            "h" -> "H"
            "n" -> "N"
            "l" -> "L"
            else -> continue@loop
        }
        break@loop
    }
    return priority
}

fun taskListInput (taskList: MutableList<MutableList<Any>>, initialOrEdit: String, editIndex: Int): Boolean {
    var taskListInputIsBlank: Boolean
    println("Input a new task (enter a blank line to end):")
    var task: String
    val listOfTaskInThisList: MutableList<Any> = mutableListOf()
    listOfTaskInThisList.clear()
    loop@while (true) {
        task = readln().trim()
        if (task.isBlank()) {
            if (listOfTaskInThisList.isEmpty()) println("The task is blank")
            taskListInputIsBlank = true
            break@loop
        } else {
            listOfTaskInThisList.add(task)
            taskListInputIsBlank = false
        }
    }
    if (listOfTaskInThisList.isNotEmpty()) {
        if (initialOrEdit == "initial") taskList.add(listOfTaskInThisList)
        else {
            for (i in listOfTaskInThisList.indices)
                taskList[editIndex].add(listOfTaskInThisList[i])
        }
        taskListInputIsBlank = false
    } else taskListInputIsBlank = true
    return taskListInputIsBlank
}

fun dateInput (): String {
    val inputRequest = "Input the date (yyyy-mm-dd):"
    var date: String
    val invalidDateMessage = "The input date is invalid"
    loop@while (true) {
        println(inputRequest)
        try {
            val dateUserInput = dateInputFormat(readln())
            date = dateUserInput.toLocalDate().toString()
        } catch (e: Exception) {
            println(invalidDateMessage)
            continue@loop
        }
        break@loop
    }
    return date
}

fun dateInputFormat (date: String): String {
    val (year, month, day) = date.split("-")
    return "${year.padStart(4, '0')}-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
}

fun timeInput (): String {
    val inputRequest = "Input the time (hh:mm):"
    var time: String
    val invalidTimeMessage = "The input time is invalid"
    loop@while (true) {
        println(inputRequest)
        try {
            val timeUserInput = timeInputFormat(readln())
            time = LocalTime.parse(timeUserInput).toString()
        } catch (e: Exception) {
            println(invalidTimeMessage)
            continue@loop
        }
        break@loop
    }
    return time
}

fun timeInputFormat (time:String): String {
    val (hours, minutes) = time.split(":")
    return "${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}"
}

fun printTaskList (taskList: MutableList<MutableList<Any>>) {
    val startOfWrappedText: String = "|    |            |       |   |   |"
    val horizontalBorder = "+----+------------+-------+---+---+--------------------------------------------+"
    val topHeader = """+----+------------+-------+---+---+--------------------------------------------+
| N  |    Date    | Time  | P | D |                   Task                     |
+----+------------+-------+---+---+--------------------------------------------+"""

    fun wrapTask (string: Any, startOfWrappedText: String, firstLine: Boolean): String {
        var task: String = ""
        for (i in 0 .. string.toString().chunked(44).lastIndex) {
            if (i == 0 && string.toString().chunked(44).lastIndex > 0) {
                task = if (firstLine) {
                    string.toString().chunked(44)[i].padEnd(44, ' ') + "|\n"
                } else startOfWrappedText + string.toString().chunked(44)[i].padEnd(44, ' ') + "|\n"
            }
            else if (i < string.toString().chunked(44).lastIndex) task += startOfWrappedText + string.toString().chunked(44)[i].padEnd(44, ' ') + "|\n"
            else if (i == 0 && string.toString().chunked(44).lastIndex == 0 && !firstLine) task = startOfWrappedText + string.toString().chunked(44)[i].padEnd(44, ' ') + "|"
            else if (i == 0) task = string.toString().chunked(44)[i].padEnd(44, ' ') + "|"
            else task += startOfWrappedText + string.toString().chunked(44)[i].padEnd(44, ' ') + "|"
        }
        return task
    }


    println(topHeader)
    for (i in 0..taskList.lastIndex) {
        if (i < 9) {
            println("| ${i+1}  | ${taskList[i][0]} | ${taskList[i][1]} | ${taskList[i][2]} | ${taskList[i][3]} |${wrapTask(taskList[i][4].toString(), startOfWrappedText, true)}")
            if (taskList[i].lastIndex > 4) {
                for (r in 5..taskList[i].lastIndex) {
                    println(wrapTask(taskList[i][r],startOfWrappedText, false))
                }
            }
        } else {
            println("| ${i+1} | ${taskList[i][0]} | ${taskList[i][1]} | ${taskList[i][2]} | ${taskList[i][3]} |${taskList[i][4].toString().chunked(44).joinToString("\n") { it.padEnd(44, ' ') }}|")
            if (taskList[i].lastIndex > 4) {
                for (r in 5..taskList[i].lastIndex) {
                    println("$startOfWrappedText${taskList[i][r].toString().chunked(44).joinToString("\n"){ it.padEnd(44, ' ') }}|")
                }
            }
        }
        println(horizontalBorder)
    }
}

fun editTask (taskList: MutableList<MutableList<Any>>) {
    val noTasksErrorMessage = "No tasks have been input"
    val taskOutOfRangeMessage = "Invalid task number"
    val inputEditMessage = "Input a field to edit (priority, date, time, task):"
    val invalidFieldMessage = "Invalid field"
    val editCompletedMessage = "The task is changed"
    var taskToBeEdited: Int
    var fieldToBeEdited: String
    loop@while (true) {
        if (taskList.isEmpty()) {
            println(noTasksErrorMessage)
            break@loop
        } else {
            try {
                println("Input the task number (1-${taskList.lastIndex + 1}):")
                taskToBeEdited = readln().toInt()
                loopb@while (true) {
                    println(inputEditMessage)
                    fieldToBeEdited = readln()
                    if (fieldToBeEdited == "priority") {
                        taskList[taskToBeEdited - 1][2] = priorityInput()
                        println(editCompletedMessage)
                        break@loopb
                    } else if (fieldToBeEdited == "date") {
                        taskList[taskToBeEdited - 1][0] = dateInput()
                        println(editCompletedMessage)
                        break@loopb
                    } else if (fieldToBeEdited == "time") {
                        taskList[taskToBeEdited - 1][1] = timeInput()
                        println(editCompletedMessage)
                        break@loopb
                    } else if (fieldToBeEdited == "task") {
                        for (i in 4..taskList[taskToBeEdited-1].lastIndex) {
                            taskList[taskToBeEdited - 1].removeAt(i)
                        }
                        taskListInput(taskList, "edit", taskToBeEdited-1)
                        println(editCompletedMessage)
                        break@loopb
                    }else {
                        println(invalidFieldMessage)
                        continue@loopb
                    }
                }
            } catch (e: Exception) {
                println(taskOutOfRangeMessage)
                continue@loop
            }
            break@loop
        }
    }
}

fun deleteTask (taskList: MutableList<MutableList<Any>>) {
    val inputDeleteMessage = "Input the task number (1-${taskList.lastIndex+1}):"
    val noTasksErrorMessage = "No tasks have been input"
    val taskOutOfRangeMessage = "Invalid task number"
    val deleteCompleteMessage ="The task is deleted"
    var taskToBeDeleted: Int
    loop@while (true) {
        if (taskList.isEmpty()) {
            println(noTasksErrorMessage)
            break@loop
        } else {
            try {
                println(inputDeleteMessage)
                taskToBeDeleted = readln().toInt()
                taskList[taskToBeDeleted-1].clear()
                println(deleteCompleteMessage)
            } catch (e: Exception) {
                println(taskOutOfRangeMessage)
                continue@loop
            }
            break@loop
        }
    }
}

fun exit () {
    exitProcess(0)
}
