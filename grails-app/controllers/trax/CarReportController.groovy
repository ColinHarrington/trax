package trax

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CarReportController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CarReport.list(params), model:[carReportCount: CarReport.count()]
    }

    def show(CarReport carReport) {
        respond carReport
    }

    def create() {
        respond new CarReport(params)
    }

    @Transactional
    def save(CarReport carReport) {
        if (carReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (carReport.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond carReport.errors, view:'create'
            return
        }

        carReport.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'carReport.label', default: 'CarReport'), carReport.id])
                redirect carReport
            }
            '*' { respond carReport, [status: CREATED] }
        }
    }

    def edit(CarReport carReport) {
        respond carReport
    }

    @Transactional
    def update(CarReport carReport) {
        if (carReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (carReport.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond carReport.errors, view:'edit'
            return
        }

        carReport.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'carReport.label', default: 'CarReport'), carReport.id])
                redirect carReport
            }
            '*'{ respond carReport, [status: OK] }
        }
    }

    @Transactional
    def delete(CarReport carReport) {

        if (carReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        carReport.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'carReport.label', default: 'CarReport'), carReport.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'carReport.label', default: 'CarReport'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
