package trax

class ReporterController {

    def report(String car, Integer data) { 
    	CarReport carReport = new CarReport(
    		carId: car, data: data).save()
    	response.status = 200
    }
}
