package org.example.eksamenkea.model;

public class ProjectCostDTO {
    private int projectId;
    private String projectName;
    private double budget;
    private String projectDescription;
    private int employeeId;
    private int materialCost;
    private int employeeCost;
    private int estimatedTimeConsumption;

    public ProjectCostDTO(int projectId, String projectName, double budget, String projectDescription, int employeeId, int materialCost, int employeeCost, int estimatedTimeConsumption) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.budget = budget;
        this.projectDescription = projectDescription;
        this.employeeId = employeeId;
        this.materialCost = materialCost;
        this.employeeCost = employeeCost;
        this.estimatedTimeConsumption = estimatedTimeConsumption;
    }

    public int getEstimatedTimeConsumption() {
        return estimatedTimeConsumption;
    }

    public void setEstimatedTimeConsumption(int estimatedTimeConsumption) {
        this.estimatedTimeConsumption = estimatedTimeConsumption;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(int materialCost) {
        this.materialCost = materialCost;
    }

    public int getEmployeeCost() {
        return employeeCost;
    }

    public void setEmployeeCost(int employeeCost) {
        this.employeeCost = employeeCost;
    }

    @Override
    public String toString() {
        return "ProjectEmployeeCostDTO{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", budget=" + budget +
                ", projectDescription='" + projectDescription + '\'' +
                ", employeeId=" + employeeId +
                ", materialCost=" + materialCost +
                ", employeeCost=" + employeeCost +
                '}';
    }
}
