package com.javaportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaportal.config.PasswordGenerator;
import com.javaportal.dto.EmployeeDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Team;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.TeamRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	private final EmployeeRepository empRepo;
	private final TeamRepository teamRepo;
	private final ModelMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;
	private final String fromEmail;

	public AdminServiceImpl(EmployeeRepository empRepo, TeamRepository teamRepo, ModelMapper mapper,
			PasswordEncoder passwordEncoder, JavaMailSender mailSender,
			@Value("${spring.mail.username}") String fromEmail) {
		this.empRepo = empRepo;
		this.teamRepo = teamRepo;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.fromEmail = fromEmail;
	}

	@Override
	public List<EmployeeDTO> getEmployeesByTeam(String teamName) throws JavaPortalException {
		Optional<Team> optTeam = teamRepo.findByTeamName(teamName);
		Team team = optTeam.orElseThrow(() -> new JavaPortalException("Service.TEAM_NOT_FOUND"));

		List<Employee> listOfEmps = empRepo.findByTeam_TeamId(team.getTeamId());
		if (listOfEmps.isEmpty())
			throw new JavaPortalException("Service.NO_EMPLOYEES_FOUND");

		List<EmployeeDTO> listOfEmpsDTO = new ArrayList<>();

		for (Employee e : listOfEmps) {
			EmployeeDTO edto = new EmployeeDTO();
			edto.setEmpId(e.getEmpId());
			edto.setName(e.getName());
			edto.setEmailId(e.getEmailId());
			edto.setContact(e.getContact());
			edto.setAddress(e.getAddress());
			edto.setAreaOfInterest(e.getAreaOfInterest());
			edto.setExpertise(e.getExpertise());
			edto.setCabinDetails(e.getCabinDetails());
			edto.setResponsibility(e.getResponsibility());
			edto.setRole(e.getRole());
			edto.setManagerId(e.getManager() == null ? null : e.getManager().getEmpId());
			edto.setTeamId(e.getTeam() == null ? null : e.getTeam().getTeamId());
			edto.setFirstLogin(e.isFirstLogin());

			listOfEmpsDTO.add(edto);
		}

		return listOfEmpsDTO;
	}

	@Override
	public EmployeeDTO getEmployee(Integer empId) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(empId);

		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

		EmployeeDTO edto = new EmployeeDTO();
		edto.setEmpId(emp.getEmpId());
		edto.setName(emp.getName());
		edto.setEmailId(emp.getEmailId());
		edto.setContact(emp.getContact());
		edto.setAddress(emp.getAddress());
		edto.setAreaOfInterest(emp.getAreaOfInterest());
		edto.setExpertise(emp.getExpertise());
		edto.setCabinDetails(emp.getCabinDetails());
		edto.setResponsibility(emp.getResponsibility());
		edto.setRole(emp.getRole());
		edto.setManagerId(emp.getManager() == null ? null : emp.getManager().getEmpId());
		edto.setTeamId(emp.getTeam() == null ? null : emp.getTeam().getTeamId());
		edto.setFirstLogin(emp.isFirstLogin());

		return edto;

	}

	@Override
	public Integer createEmployee(EmployeeDTO empDTO) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findByEmailId(empDTO.getEmailId());
		if (optEmp.isPresent()) {
			throw new JavaPortalException("Service.EMPLOYEE_ALREADY_PRESENT");
		}

		Employee emp = mapper.map(empDTO, Employee.class);

		if (empDTO.getManagerId() != null) {
			Employee manager = empRepo.findById(empDTO.getManagerId())
					.orElseThrow(() -> new JavaPortalException("Service.MANAGER_NOT_FOUND"));
			emp.setManager(manager);
		}

		if (empDTO.getTeamId() != null) {
			Team team = teamRepo.findById(empDTO.getTeamId())
					.orElseThrow(() -> new JavaPortalException("Service.TEAM_NOT_FOUND"));
			emp.setTeam(team);
		}

		String password = PasswordGenerator.generateStrongPassword();

		String htmlMsg = "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">" + "<p>Dear "
				+ emp.getName() + ",</p>"
				+ "<p>We are pleased to inform you that your account has been successfully created in the Java Track Portal.</p>"
				+ "<p><strong>Your temporary login credentials are as follows:</strong></p>" + "<ul>"
				+ "<li><strong>Email:</strong> " + emp.getEmailId() + "</li>" + "<li><strong>Password:</strong> "
				+ password + "</li>" + "</ul>"
				+ "<p>Please make sure to log in and change your password immediately for security purposes.</p>"
				+ "<p>If you have any questions or need assistance, feel free to reach out to the support team.</p>"
				+ "<br>" + "<p>Best regards,<br>Java Track Portal Team</p>" + "</div>";

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(emp.getEmailId());
			helper.setSubject("Welcome to Java Track Portal - Account Created Successfully");
			helper.setText(htmlMsg, true); // true = HTML email

			mailSender.send(message);

		} catch (MessagingException e) {
			throw new JavaPortalException("Email sending failed: " + e.getMessage());
		}

		emp.setPassword(passwordEncoder.encode(password));

		empRepo.save(emp);
		return emp.getEmpId();
	}

	@Override
	public boolean updateEmployee(EmployeeDTO empDTO) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(empDTO.getEmpId());
		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

		emp.setCabinDetails(empDTO.getCabinDetails());
		emp.setRole(empDTO.getRole());
		emp.setResponsibility(empDTO.getResponsibility());

		empRepo.save(emp);
		return true;
	}

	@Override
	public Integer deleteEmployee(Integer empId) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(empId);
		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

		Integer teamId = emp.getTeam().getTeamId();

		emp.setManager(null);
		emp.setTeam(null);
		empRepo.delete(emp);

		Optional<Team> optTeam = teamRepo.findById(teamId);
		Team team = optTeam.orElseThrow(() -> new JavaPortalException("Service.TEAM_NOT_FOUND"));
		team.setTeamSize(team.getTeamSize() - 1);
		teamRepo.save(team);

		return empId;
	}

	// for rendering the team names for admin to choose from
	@Override
	public List<String> getAllTeamNames() throws JavaPortalException {
		List<Team> listOfTeams = teamRepo.findAll();
		if (listOfTeams.isEmpty())
			throw new JavaPortalException("Service.NO_TEAMS_FOUND");

		List<String> listOfTeamNames = new ArrayList<>();
		for (Team t : listOfTeams) {
			listOfTeamNames.add(t.getTeamName());
		}

		return listOfTeamNames;
	}
}
