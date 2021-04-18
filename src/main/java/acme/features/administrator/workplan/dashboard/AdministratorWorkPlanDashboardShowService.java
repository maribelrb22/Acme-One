package acme.features.administrator.workplan.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.forms.workplan.dashboard.WorkplanDashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorWorkPlanDashboardShowService implements AbstractShowService<Administrator, WorkplanDashboard> {

	@Autowired
	AdministratorWorkPlanDashboardRepository administratorWorkPlanDashboardRepository;
	
	@Override
	public boolean authorise(Request<WorkplanDashboard> request) {
		assert request != null;
        return true;
	}

	@Override
	public void unbind(Request<WorkplanDashboard> request, WorkplanDashboard entity, Model model) {
		assert request != null;
        assert entity != null;
        assert model != null;
        request.unbind(entity, model, //
        		"totalNumberOfPublicWorkplans", "totalNumberOfPrivateWorkplans", //
        		"totalNumberOfFinishedWorkplans", "totalNumberOfNonFinishedWorkplans", //
        		"averageNumberOfPeriods", "minimumNumberOfPeriods", "maximumNumberOfPeriods",  //
        		"averageNumberOfWorkloads", "minimumNumberOfWorkloads", "maximumNumberOfWorkloads");
	}

	@Override
	public WorkplanDashboard findOne(Request<WorkplanDashboard> request) {
		assert request != null;
		WorkplanDashboard result;
		
		Integer totalNumberOfPublicWorkplans = this.administratorWorkPlanDashboardRepository.totalNumberOfPublicWorkplans();
		Integer totalNumberOfPrivateWorkplans = this.administratorWorkPlanDashboardRepository.totalNumberOfPrivateWorkplans();
		List<WorkPlan> workplans = this.administratorWorkPlanDashboardRepository.findAllWorkplans();
		List<WorkPlan> workplansFinished = workplans.stream().filter(x->x.isFinished().equals(true)).collect(Collectors.toList());
		List<WorkPlan> workplansNonFinished = workplans.stream().filter(x->x.isFinished().equals(false)).collect(Collectors.toList());
		Double averageNumberOfWorkloads = workplans.stream().mapToDouble(x->x.getWorkload()).average().orElse(0.);
		Double minimumNumberOfWorkloads = workplans.stream().mapToDouble(x->x.getWorkload()).min().orElse(0.);
		Double maximumNumberOfWorkloads = workplans.stream().mapToDouble(x->x.getWorkload()).max().orElse(0.);
		int milisecondsByDay = 86400000;
		Double averageNumberOfPeriods = workplans.stream().mapToDouble(x->x.getEnd().getTime()/milisecondsByDay - x.getBegin().getTime()/milisecondsByDay).average().orElse(0.);
		Double minimumNumberOfPeriods = workplans.stream().mapToDouble(x->x.getEnd().getTime()/milisecondsByDay - x.getBegin().getTime()/milisecondsByDay).min().orElse(0.);
		Double maximumNumberOfPeriods = workplans.stream().mapToDouble(x->x.getEnd().getTime()/milisecondsByDay - x.getBegin().getTime()/milisecondsByDay).max().orElse(0.);

		result = new WorkplanDashboard();
		result.setTotalNumberOfPublicWorkplans(totalNumberOfPublicWorkplans);
		result.setTotalNumberOfPrivateWorkplans(totalNumberOfPrivateWorkplans);
		result.setTotalNumberOfFinishedWorkplans(workplansFinished.size());
		result.setTotalNumberOfNonFinishedWorkplans(workplansNonFinished.size());
		result.setAverageNumberOfWorkloads(averageNumberOfWorkloads);
		result.setMinimumNumberOfWorkloads(minimumNumberOfWorkloads);
		result.setMaximumNumberOfWorkloads(maximumNumberOfWorkloads);
		result.setAverageNumberOfPeriods(averageNumberOfPeriods);
		result.setMinimumNumberOfPeriods(minimumNumberOfPeriods);
		result.setMaximumNumberOfPeriods(maximumNumberOfPeriods);
		
		return result;
	}

}
