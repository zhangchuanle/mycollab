/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view;

import java.util.GregorianCalendar;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProblemSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.ProblemEvent;
import com.esofthead.mycollab.module.project.events.RiskEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.bug.TrackerPresenter;
import com.esofthead.mycollab.module.project.view.file.IFilePresenter;
import com.esofthead.mycollab.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter;
import com.esofthead.mycollab.module.project.view.parameters.FileScreenData;
import com.esofthead.mycollab.module.project.view.parameters.MilestoneScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProblemScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectMemberScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.RiskScreenData;
import com.esofthead.mycollab.module.project.view.parameters.StandupScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TimeTrackingScreenData;
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter;
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter;
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter;
import com.esofthead.mycollab.module.project.view.standup.IStandupPresenter;
import com.esofthead.mycollab.module.project.view.task.TaskPresenter;
import com.esofthead.mycollab.module.project.view.time.ITimeTrackingPresenter;
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.esofthead.mycollab.module.project.view.user.ProjectListComponent;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractCssPageView;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet.TabImpl;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
@ViewComponent
public class ProjectViewImpl extends AbstractCssPageView implements ProjectView {

	private final ProjectVerticalTabsheet myProjectTab;
	private final HorizontalLayout topPanel;
	private ProjectDashboardPresenter dashboardPresenter;
	private MessagePresenter messagePresenter;
	private MilestonePresenter milestonesPresenter;
	private TaskPresenter taskPresenter;
	private TrackerPresenter trackerPresenter;
	private IFilePresenter filePresenter;
	private IProblemPresenter problemPresenter;
	private IRiskPresenter riskPresenter;
	private ITimeTrackingPresenter timePresenter;
	private UserSettingPresenter userPresenter;
	private IStandupPresenter standupPresenter;
	private final ProjectBreadcrumb breadCrumb;
	private PopupButton controlsBtn;
	private ProjectListComponent prjList;

	public ProjectViewImpl() {
		this.setWidth("100%");

		// this.setMargin(false);
		this.addStyleName("main-content-wrapper");
		this.addStyleName("projectDashboardView");
		this.setVerticalTabsheetFix(true);

		breadCrumb = ViewManager.getView(ProjectBreadcrumb.class);

		topPanel = new HorizontalLayout();
		topPanel.setWidth("100%");
		topPanel.setMargin(true);
		topPanel.setStyleName("top-panel");

		myProjectTab = new ProjectVerticalTabsheet();
		myProjectTab.setSizeFull();
		myProjectTab.setNavigatorWidth("100%");
		myProjectTab.setNavigatorStyleName("sidebar-menu");
		myProjectTab.setContainerStyleName("tab-content");
		myProjectTab.setHeight(null);

		VerticalLayout contentWrapper = myProjectTab.getContentWrapper();
		contentWrapper.addStyleName("main-content");
		contentWrapper.addComponentAsFirst(topPanel);

		prjList = new ProjectListComponent();
		CssLayout navigatorWrapper = myProjectTab.getNavigatorWrapper();
		navigatorWrapper.addComponentAsFirst(prjList);
		navigatorWrapper.setWidth("250px");

		buildComponents();
		this.addComponent(myProjectTab);
	}

	private void buildComponents() {
		myProjectTab.addTab(constructProjectDashboardComponent(), "dashboard",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));

		myProjectTab.addTab(constructProjectMessageComponent(), "message",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE));

		myProjectTab.addTab(constructProjectMilestoneComponent(), "milestone",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE));

		myProjectTab.addTab(constructTaskDashboardComponent(), "task",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK));

		myProjectTab.addTab(constructProjectBugComponent(), "bug",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG));

		myProjectTab.addTab(constructProjectFileComponent(), "file",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE));

		myProjectTab.addTab(constructProjectRiskComponent(), "risk",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK));

		myProjectTab.addTab(constructProjectProblemComponent(), "problem",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM));

		myProjectTab.addTab(constructTimeTrackingComponent(), "time",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME));

		myProjectTab.addTab(constructProjectStandupMeeting(), "standup",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP));

		myProjectTab.addTab(constructProjectUsers(), "member",
				AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MEMBER));

		myProjectTab
				.addSelectedTabChangeListener(new SelectedTabChangeListener() {

					@Override
					public void selectedTabChange(SelectedTabChangeEvent event) {
						Tab tab = ((ProjectVerticalTabsheet) event.getSource())
								.getSelectedTab();
						String caption = ((TabImpl) tab).getTabId();
						if ("message".equals(caption)) {
							messagePresenter.go(ProjectViewImpl.this, null);
						} else if ("milestone".equals(caption)) {
							MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
							searchCriteria.setProjectId(new NumberSearchField(
									SearchField.AND, CurrentProjectVariables
											.getProjectId()));
							gotoMilestoneView(new MilestoneScreenData.Search(
									searchCriteria));
						} else if ("task".equals(caption)) {
							taskPresenter.go(ProjectViewImpl.this, null);
						} else if ("bug".equals(caption)) {
							gotoBugView(null);
						} else if ("risk".equals(caption)) {
							RiskSearchCriteria searchCriteria = new RiskSearchCriteria();
							searchCriteria.setProjectId(new NumberSearchField(
									SearchField.AND, CurrentProjectVariables
											.getProjectId()));
							gotoRiskView(new RiskScreenData.Search(
									searchCriteria));
						} else if ("file".equals(caption)) {
							filePresenter.go(ProjectViewImpl.this,
									new FileScreenData.GotoDashboard());
						} else if ("problem".equals(caption)) {
							ProblemSearchCriteria searchCriteria = new ProblemSearchCriteria();
							searchCriteria.setProjectId(new NumberSearchField(
									SearchField.AND, CurrentProjectVariables
											.getProjectId()));
							problemPresenter
									.go(ProjectViewImpl.this,
											new ProblemScreenData.Search(
													searchCriteria));
						} else if ("dashboard".equals(caption)) {
							dashboardPresenter.go(ProjectViewImpl.this, null);
						} else if ("member".equals(caption)) {
							ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
							criteria.setProjectId(new NumberSearchField(
									CurrentProjectVariables.getProjectId()));
							criteria.setStatus(new StringSearchField(
									ProjectMemberStatusConstants.ACTIVE));
							gotoUsersAndGroup(new ProjectMemberScreenData.Search(
									criteria));
						} else if ("time".equals(caption)) {
							ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
							searchCriteria.setProjectId(new NumberSearchField(
									CurrentProjectVariables.getProjectId()));
							searchCriteria.setRangeDate(ItemTimeLoggingSearchCriteria
									.getCurrentRangeDateOfWeekSearchField());
							gotoTimeTrackingView(new TimeTrackingScreenData.Search(
									searchCriteria));
						} else if ("standup".equals(caption)) {
							StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();
							criteria.setProjectId(new NumberSearchField(
									CurrentProjectVariables.getProjectId()));
							criteria.setOnDate(new DateSearchField(
									SearchField.AND, DateSearchField.EQUAL,
									new GregorianCalendar().getTime()));
							standupPresenter.go(ProjectViewImpl.this,
									new StandupScreenData.Search(criteria));
						}

					}
				});
	}

	@Override
	public void gotoUsersAndGroup(ScreenData<?> data) {
		userPresenter.go(ProjectViewImpl.this, data);
	}

	@Override
	public void gotoTaskList(ScreenData<?> data) {
		taskPresenter.go(ProjectViewImpl.this, data);
	}

	@Override
	public void gotoRiskView(ScreenData<?> data) {
		riskPresenter.go(ProjectViewImpl.this, data);
	}

	public void gotoTimeTrackingView(ScreenData<?> data) {
		timePresenter.go(ProjectViewImpl.this, data);
	}

	@Override
	public void gotoBugView(ScreenData<?> data) {
		trackerPresenter.go(ProjectViewImpl.this, data);
	}

	@Override
	public void gotoMilestoneView(ScreenData<?> data) {
		milestonesPresenter.go(ProjectViewImpl.this, data);
	}

	@Override
	public void gotoStandupReportView(ScreenData<?> data) {
		standupPresenter.go(ProjectViewImpl.this, data);
	}

	private Component constructProjectDashboardComponent() {
		dashboardPresenter = PresenterResolver
				.getPresenter(ProjectDashboardPresenter.class);
		return dashboardPresenter.initView();
	}

	private Component constructProjectUsers() {
		userPresenter = PresenterResolver
				.getPresenter(UserSettingPresenter.class);
		return userPresenter.initView();
	}

	private Component constructProjectMessageComponent() {
		messagePresenter = PresenterResolver
				.getPresenter(MessagePresenter.class);
		return messagePresenter.initView();
	}

	private Component constructProjectMilestoneComponent() {
		milestonesPresenter = PresenterResolver
				.getPresenter(MilestonePresenter.class);
		return milestonesPresenter.initView();
	}

	private Component constructProjectRiskComponent() {
		riskPresenter = PresenterResolver.getPresenter(IRiskPresenter.class);
		return riskPresenter.initView();
	}

	private Component constructProjectProblemComponent() {
		problemPresenter = PresenterResolver
				.getPresenter(IProblemPresenter.class);
		return problemPresenter.initView();
	}

	private Component constructTimeTrackingComponent() {
		timePresenter = PresenterResolver
				.getPresenter(ITimeTrackingPresenter.class);
		return timePresenter.initView();
	}

	private Component constructProjectStandupMeeting() {
		standupPresenter = PresenterResolver
				.getPresenter(IStandupPresenter.class);
		return standupPresenter.initView();
	}

	private Component constructTaskDashboardComponent() {
		taskPresenter = PresenterResolver.getPresenter(TaskPresenter.class);
		return taskPresenter.initView();
	}

	private Component constructProjectBugComponent() {
		trackerPresenter = PresenterResolver
				.getPresenter(TrackerPresenter.class);
		return trackerPresenter.initView();
	}

	private Component constructProjectFileComponent() {
		filePresenter = PresenterResolver.getPresenter(IFilePresenter.class);
		return filePresenter.initView();
	}

	@Override
	public void constructProjectHeaderPanel(final SimpleProject project,
			PageActionChain pageActionChain) {
		topPanel.removeAllComponents();

		topPanel.addComponent(breadCrumb);
		topPanel.setComponentAlignment(breadCrumb, Alignment.MIDDLE_LEFT);
		topPanel.setExpandRatio(breadCrumb, 1.0f);

		breadCrumb.setProject(project);

		/*
		 * Button quickActionBtn = new Button("", new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * controlsBtn.setPopupVisible(true); } });
		 * 
		 * quickActionBtn.setDescription("Quick action");
		 */
		controlsBtn = new PopupButton();
		controlsBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/quick_action_edited.png"));
		controlsBtn.addStyleName(UIConstants.THEME_BLANK_LINK);
		// controlsBtn.setWidthPopupButton("0");

		VerticalLayout popupButtonsControl = new VerticalLayout();
		popupButtonsControl.setSpacing(true);
		popupButtonsControl.setWidth("150px");

		Button createPhaseBtn = new Button(
				AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE),
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new MilestoneEvent.GotoAdd(
										ProjectViewImpl.this, null));
					}
				});
		createPhaseBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.MILESTONES));
		createPhaseBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/milestone.png"));
		createPhaseBtn.setStyleName("link");
		popupButtonsControl.addComponent(createPhaseBtn);

		Button createBugBtn = new Button(
				AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG),
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new BugEvent.GotoAdd(this, null));
					}
				});
		createBugBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
		createBugBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/bug.png"));
		createBugBtn.setStyleName("link");
		popupButtonsControl.addComponent(createBugBtn);

		Button createRiskBtn = new Button(
				AppContext.getMessage(RiskI18nEnum.BUTTON_NEW_RISK),
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new RiskEvent.GotoAdd(this, null));
					}
				});
		createRiskBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.RISKS));
		createRiskBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/risk.png"));
		createRiskBtn.setStyleName("link");
		popupButtonsControl.addComponent(createRiskBtn);

		Button createProblemBtn = new Button(
				AppContext.getMessage(ProblemI18nEnum.BUTTON_NEW_PROBLEM),
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ProblemEvent.GotoAdd(this, null));
					}
				});
		createProblemBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PROBLEMS));
		createProblemBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/problem.png"));
		createProblemBtn.setStyleName("link");
		popupButtonsControl.addComponent(createProblemBtn);

		Button editProjectBtn = new Button(
				AppContext
						.getMessage(ProjectCommonI18nEnum.BUTTON_EDIT_PROJECT),
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						dashboardPresenter.go(ProjectViewImpl.this,
								new ProjectScreenData.Edit(project));
					}
				});
		editProjectBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PROJECT));
		editProjectBtn.setIcon(MyCollabResource
				.newResource("icons/16/project/edit.png"));
		editProjectBtn.setStyleName("link");
		popupButtonsControl.addComponent(editProjectBtn);

		if (CurrentProjectVariables
				.canAccess(ProjectRolePermissionCollections.PROJECT)) {
			Button deleteProjectBtn = new Button(
					AppContext
							.getMessage(ProjectCommonI18nEnum.BUTTON_DELETE_PROJECT),
					new Button.ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							controlsBtn.setPopupVisible(false);
							ConfirmDialogExt.show(
									UI.getCurrent(),
									AppContext.getMessage(
											GenericI18Enum.DIALOG_DELETE_TITLE,
											SiteConfiguration.getSiteName()),
									AppContext
											.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE),
									AppContext
											.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
									AppContext
											.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
									new ConfirmDialog.Listener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClose(ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {
												ProjectService projectService = ApplicationContextUtil
														.getSpringBean(ProjectService.class);
												projectService.removeWithSession(
														CurrentProjectVariables
																.getProjectId(),
														AppContext
																.getUsername(),
														AppContext
																.getAccountId());
												EventBus.getInstance()
														.fireEvent(
																new ShellEvent.GotoProjectModule(
																		this,
																		null));
											}
										}
									});
						}
					});
			deleteProjectBtn.setEnabled(CurrentProjectVariables
					.canAccess(ProjectRolePermissionCollections.PROJECT));
			deleteProjectBtn.setIcon(MyCollabResource
					.newResource("icons/16/project/delete_project.png"));
			deleteProjectBtn.setStyleName("link");
			popupButtonsControl.addComponent(deleteProjectBtn);
		}

		controlsBtn.setContent(popupButtonsControl);
		controlsBtn.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);

		topPanel.addComponent(controlsBtn);
		topPanel.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

		prjList.showProjects();
	}

	@Override
	public Component gotoSubView(String viewId) {
		PageView component = (PageView) myProjectTab.selectTab(viewId);
		return component;
	}

}