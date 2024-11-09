package seg3x02.employeeGql.resolvers

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*

@Controller
class EmployeesResolver(private val employeesRepository: EmployeesRepository) {
    @QueryMapping
    fun employees(): List<Employee> {
        return employeesRepository.findAll()
    }

    @QueryMapping
    fun employeeById(@Argument employeeId: String): Employee? {
        val emp = employeesRepository.findById(employeeId)
        return emp.orElse(null)
    }

    @MutationMapping
    fun newEmployee(@Argument("createEmployeeInput") input: CreateEmployeeInput) : Employee {
        if (input.name != null &&
            input.city != null &&
            input.dateOfBirth != null &&
            input.salary != null) {
            val emp = Employee(input.name, input.dateOfBirth, input.city, input.salary, input.gender, input.email)
            emp.id = UUID.randomUUID().toString()
            employeesRepository.save(emp)
            return emp
        } else {
            throw Exception("Invalid input")
        }
    }

    @MutationMapping
    fun deleteEmployee(@Argument("id") id: String) : Boolean {
        employeesRepository.deleteById(id)
        return true
    }

    @MutationMapping
    fun updateEmployee(@Argument id: String, @Argument("createEmployeeInput") input: CreateEmployeeInput) : Employee {
        val employee = employeesRepository.findById(id)
        employee.ifPresent {
            if (input.name != null) {
                it.name = input.name
            }
            if (input.dateOfBirth != null) {
                it.dateOfBirth = input.dateOfBirth
            }
            if (input.city != null) {
                it.city = input.city
            }
            if (input.salary != null) {
                it.salary = input.salary
            }
            if (input.email != null) {
                it.email = input.email
            }
            if (input.gender != null) {
                it.gender = input.gender
            }
            employeesRepository.save(it)
        }
        return employee.get()
    }

}
