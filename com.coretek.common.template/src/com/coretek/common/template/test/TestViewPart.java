/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * 此视图仅仅用于测试使用
 * 
 * @author 孙大巍 2011-12-26
 */
public class TestViewPart extends ViewPart
{

	private TreeViewer		viewer;

	private Text			text;

	private Composite		panel;

	private TableViewer		tableViewer;

	private TableTreeViewer	ttv;

	private TableItem		selectedTableItem;

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
	 */
	public TestViewPart()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		GridData grid = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(grid);
		viewer = new TreeViewer(container, SWT.NONE);
		grid = new GridData(GridData.FILL_BOTH);
		grid.heightHint = 200;
		grid.widthHint = 500;
		viewer.getTree().setLayoutData(grid);
		this.viewer.setContentProvider(new ContentProvider());
		this.viewer.setLabelProvider(new LabelProvider());

		this.panel = new Composite(container, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 200;
		panel.setBackground(ColorConstants.gray);
		text = new Text(panel, SWT.MULTI);
		grid = new GridData(GridData.FILL_BOTH);
		grid.heightHint = 200;
		grid.widthHint = 500;
		text.setLayoutData(grid);
		layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);
		this.viewer.addSelectionChangedListener(new MySelectionListener());

		Composite panel2 = new Composite(container, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 300;
		panel2.setBackground(ColorConstants.black);
		panel2.setLayoutData(grid);
		layout = new GridLayout();
		layout.numColumns = 5;
		panel2.setLayout(layout);

		this.tableViewer = new TableViewer(panel2, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 100;
		grid.widthHint = 500;
		grid.horizontalSpan = 5;
		this.tableViewer.getTable().setLayoutData(grid);
		this.tableViewer.setLabelProvider(new TableLabelProvider());
		this.tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.getTable().setBackground(ColorConstants.gray);
		tableViewer.getTable().redraw();
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.getTable().setHeaderVisible(true);

		try
		{
			Class clazz = Class.forName("com.coretek.spte.topic.ContainedSignal");
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				FieldRules fieldRule = field.getAnnotation(FieldRules.class);
				if (fieldRule == null)
					continue;
				TableColumn tc = new TableColumn(tableViewer.getTable(), SWT.LEFT);

				if (fieldRule.xmlName() != null)
				{
					tc.setText(fieldRule.xmlName());
					tc.setWidth(100);

				}

			}

		} catch (ClassNotFoundException e)
		{

			e.printStackTrace();
		}

		ttv = new TableTreeViewer(panel2, SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 400;
		grid.widthHint = 500;
		grid.horizontalSpan = 5;
		ttv.getTableTree().setLayoutData(grid);
		ttv.getTableTree().setVisible(true);
		ttv.getTableTree().getTable().setHeaderVisible(true);
		ttv.getTableTree().getTable().setLinesVisible(true);
		ttv.setUseHashlookup(true);
		ttv.setLabelProvider(new TableLabelProvider2());
		ttv.setContentProvider(new ContentProvider2());
		ttv.getTableTree().getTable().addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				selectedTableItem = (TableItem) e.item;
			}

		});

		ttv.getTableTree().addSelectionListener(new SelectionListener()
		{

			private TableTreeItem	item;

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				final TableTreeItem item = (TableTreeItem) e.item;
				if (this.item == item)
					return;
				this.item = item;
				Entity entity = (Entity) item.getData();
				Control[] controls = ttv.getTableTree().getTable().getChildren();
				for (Control control : controls)
				{
					control.dispose();
				}
				if (entity.getClass().getName().equals("com.coretek.spte.ContainedSignal"))
				{
					final TableItem[] tableItems = item.getParent().getTable().getSelection();
					if (entity.getClass().getName().equals("com.coretek.spte.ContainedSignal"))
					{
						try
						{
							Field field = entity.getClass().getDeclaredField("unitCode");
							if (field != null)
							{
								field.setAccessible(true);
								Object code = field.get(entity);
								if (code != null && code.toString().trim().length() != 0)
								{

									// TODO 查找单位
									TableEditor editor = new TableEditor(ttv.getTableTree().getTable());

									editor.horizontalAlignment = SWT.LEFT;
									editor.grabHorizontal = true;
									editor.grabVertical = true;
									editor.setColumn(6);
									final CCombo units = new CCombo(ttv.getTableTree().getTable(), SWT.RIGHT);

									units.setEditable(false);
									units.add("aaa");
									units.add("bbb");
									units.add("ccc");
									units.add("ddd");
									units.add("fff");

									editor.setEditor(units, tableItems[0], 6);
									units.setText(tableItems[0].getText(6));
									units.addModifyListener(new ModifyListener()
									{

										@Override
										public void modifyText(ModifyEvent e)
										{
											tableItems[0].setText(6, units.getText());

										}

									});
									editor = new TableEditor(ttv.getTableTree().getTable());
									editor.horizontalAlignment = SWT.LEFT;
									editor.grabHorizontal = true;
									editor.grabVertical = true;
									Button btn = new Button(ttv.getTableTree().getTable(), SWT.PUSH);
									btn.computeSize(40, ttv.getTableTree().getTable().getItemHeight());
									btn.setText("计算器");
									editor.setEditor(btn, tableItems[0], 8);

									btn.addMouseListener(new MouseListener()
									{

										@Override
										public void mouseDoubleClick(MouseEvent e)
										{

										}

										@Override
										public void mouseDown(MouseEvent e)
										{
											new CaculatorDialog(Display.getCurrent().getActiveShell()).open();

										}

										@Override
										public void mouseUp(MouseEvent e)
										{

										}

									});
								}

							}
							TableEditor textEditor = new TableEditor(ttv.getTableTree().getTable());
							textEditor.horizontalAlignment = SWT.LEFT;
							textEditor.grabHorizontal = true;
							textEditor.grabVertical = true;
							textEditor.setColumn(7);

							final Text text = new Text(ttv.getTableTree().getTable(), SWT.LEFT);
							text.setText(tableItems[0].getText(7));
							textEditor.setEditor(text, tableItems[0], 7);
							text.setFocus();
							text.setSize(100, 20);
							text.selectAll();
							text.addModifyListener(new ModifyListener()
							{

								@Override
								public void modifyText(ModifyEvent e)
								{
									tableItems[0].setText(7, text.getText());

								}

							});
						} catch (SecurityException e1)
						{

							e1.printStackTrace();
						} catch (NoSuchFieldException e1)
						{

							e1.printStackTrace();
						} catch (IllegalArgumentException e1)
						{

							e1.printStackTrace();
						} catch (IllegalAccessException e1)
						{

							e1.printStackTrace();
						}
					}
				}

			}

		});

		try
		{
			Class clazz = Class.forName("com.coretek.spte.topic.ContainedSignal");
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				FieldRules fieldRule = field.getAnnotation(FieldRules.class);
				if (fieldRule == null)
					continue;
				TableColumn tc = new TableColumn(ttv.getTableTree().getTable(), SWT.LEFT);
				if (fieldRule.xmlName() != null)
				{
					tc.setText(fieldRule.xmlName());
					tc.setWidth(100);
					tc.setResizable(true);
				}

			}
			TableColumn tc = new TableColumn(ttv.getTableTree().getTable(), SWT.LEFT);
			tc.setText("发送值");
			tc.setWidth(100);
			tc.setResizable(true);

			tc = new TableColumn(ttv.getTableTree().getTable(), SWT.LEFT);
			tc.setText("");
			tc.setWidth(100);
			tc.setResizable(true);

		} catch (ClassNotFoundException e)
		{

			e.printStackTrace();
		}

		ttv.getTableTree().getTable().addMouseListener(new MouseListener()
		{

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				if (ttv.getTableTree().getTable().getMenu() != null)
					ttv.getTableTree().getTable().getMenu().dispose();
				if (e.button == 3)
				{
					Menu menu = new Menu(ttv.getTableTree().getTable());
					MenuItem newItem = new MenuItem(menu, SWT.NONE);
					newItem.setText("上移");
					newItem.addSelectionListener(new SelectionListener()
					{

						@Override
						public void widgetDefaultSelected(SelectionEvent e)
						{

						}

						@Override
						public void widgetSelected(SelectionEvent e)
						{
							TableTreeItem tableTreeItem = ttv.getTableTree().getSelection()[0];
							Entity entity = (Entity) tableTreeItem.getData();
							Entity parent = entity.getParent();
							int index = parent.getChildren().indexOf(entity);
							// 上移时，元素不能为开始元素
							if (index >= 0)
							{
								parent.getChildren().remove(index);
								parent.getChildren().add(index - 1, entity);
								ttv.refresh();
							}
						}

					});
					newItem = new MenuItem(menu, SWT.NONE);
					newItem.setText("下移");
					newItem.addSelectionListener(new SelectionListener()
					{

						@Override
						public void widgetDefaultSelected(SelectionEvent e)
						{

						}

						@Override
						public void widgetSelected(SelectionEvent e)
						{
							TableTreeItem tableTreeItem = ttv.getTableTree().getSelection()[0];
							Entity entity = (Entity) tableTreeItem.getData();
							Entity parent = entity.getParent();
							int index = parent.getChildren().indexOf(entity);
							// 下移时，元素不能是末尾元素
							if (parent.getChildren().get(index + 1) != null)
							{
								parent.getChildren().remove(index);
								parent.getChildren().add(index + 1, entity);
								ttv.refresh();
							}

						}

					});
					ttv.getTableTree().getTable().setMenu(menu);
				}

			}

			@Override
			public void mouseUp(MouseEvent e)
			{

			}

		});
	}

	public class MySelectionListener implements ISelectionChangedListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
		 * (org.eclipse.jface.viewers.SelectionChangedEvent) <br/> <b>作者</b> 孙大巍
		 * </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event)
		{
			Object obj = event.getSource();
			if (obj instanceof TreeViewer)
			{
				TreeItem[] items = viewer.getTree().getSelection();
				Entity entity = (Entity) items[0].getData();
				EntityRules rule = entity.getClass().getAnnotation(EntityRules.class);
				if (rule.dragable())
				{
					text.setText(entity.toString());

					try
					{
						Field field = entity.getClass().getDeclaredField("topic");
						field.setAccessible(true);
						Entity target = (Entity) field.get(entity);// 主题
						Entity[] children = target.getChildren().toArray(new Entity[1]);
						Entity containedSignals = children[0];// 包含信号

						tableViewer.setInput(containedSignals.getChildren());
						tableViewer.getTable().redraw();
					} catch (SecurityException e)
					{

						e.printStackTrace();
					} catch (NoSuchFieldException e)
					{

						e.printStackTrace();
					} catch (IllegalArgumentException e)
					{

						e.printStackTrace();
					} catch (IllegalAccessException e)
					{

						e.printStackTrace();
					}

				}

			}

		}

	}

	class TableContentProvider implements IStructuredContentProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof Set)
			{
				Set list = (Set) inputElement;
				return list.toArray();
			}
			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object) <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

	}

	class TableLabelProvider implements ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			Entity entity = (Entity) element;

			Field[] fields = entity.getClass().getDeclaredFields();
			if (columnIndex >= fields.length)
			{
				return null;
			}
			Field field = fields[columnIndex];

			try
			{
				field.setAccessible(true);
				if (field.get(entity) == null)
					return "";
				return field.get(entity).toString();
			} catch (IllegalArgumentException e)
			{

				e.printStackTrace();
			} catch (IllegalAccessException e)
			{

				e.printStackTrace();
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-27
		 */
		@Override
		public void addListener(ILabelProviderListener listener)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
		 * 2011-12-27
		 */
		@Override
		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-27
		 */
		@Override
		public void removeListener(ILabelProviderListener listener)
		{

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus() <br/> <b>作者</b> 孙大巍
	 * </br> <b>日期</b> 2011-12-26
	 */
	@Override
	public void setFocus()
	{

	}

	public void addInput(File targetFile, File rulesFile)
	{
		this.viewer.setInput(null);
		this.viewer.refresh();
		List<Entity> entities = TemplateEngine.getEngine().parse(targetFile, rulesFile).getAllEntities();// ClazzManager.getManager().getAllEntities();
		List<Entity> targets = new ArrayList<Entity>();
		for (Entity entity : entities)
		{
			if (entity.getClass().getName().equals("com.coretek.spte.Fighter"))
			{
				targets.add(entity);
				break;
			}
		}
		this.viewer.setInput(targets);
		this.ttv.setInput(targets);
	}

	public class LabelProvider implements ILabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Image getImage(Object element)
		{

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public String getText(Object element)
		{
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;
				EntityRules rule = entity.getClass().getAnnotation(EntityRules.class);
				String displayField = rule.displayField();
				if (displayField != null && displayField.trim().length() != 0 && !"null".equals(displayField))
				{
					try
					{
						Field field = entity.getClass().getDeclaredField(displayField);
						if (field != null)
						{
							field.setAccessible(true);
							return field.get(entity) + "(" + entity.getXmlName() + ")";
						} else
						{
							return entity.getXmlName();
						}

					} catch (SecurityException e)
					{

						e.printStackTrace();
					} catch (NoSuchFieldException e)
					{

						e.printStackTrace();
					} catch (IllegalArgumentException e)
					{

						e.printStackTrace();
					} catch (IllegalAccessException e)
					{

						e.printStackTrace();
					}
				}
				entity.getXmlName();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-26
		 */
		@Override
		public void addListener(ILabelProviderListener listener)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
		 * 2011-12-26
		 */
		@Override
		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-26
		 */
		@Override
		public void removeListener(ILabelProviderListener listener)
		{

		}

	}

	public class ContentProvider implements ITreeContentProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object) <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object[] getChildren(Object parentElement)
		{
			if (parentElement instanceof Entity)
			{
				Entity entity = (Entity) parentElement;
				if (entity.getClass().getName().equals("com.coretek.spte.Fighter"))
				{
					List<Entity> entities = new ArrayList<Entity>();
					for (Entity child : entity.getChildren())
					{
						if (child.getClass().getName().equals("com.coretek.spte.Version"))
						{
							entities.add(child);
						}
					}
					return entities.toArray();
				}
				if (entity.getClass().getName().equals("com.coretek.spte.Version"))
				{
					List<Entity> entities = new ArrayList<Entity>();
					for (Entity child : entity.getChildren())
					{
						if (child.getClass().getName().equals("com.coretek.spte.FunctionDomains"))
						{
							entities.addAll(child.getChildren());
						}
					}
					return entities.toArray();
				}
				if (entity.getClass().getName().equals("com.coretek.spte.FunctionDomains"))
				{

					return entity.getChildren().toArray();
				}
				return entity.getLogicChildren().toArray();
			}
			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object getParent(Object element)
		{
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;
				return entity.getLogicParent();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public boolean hasChildren(Object element)
		{
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;
				if (entity.getClass().getName().equals("com.coretek.spte.Version") || entity.getClass().getName().equals("com.coretek.spte.FunctionDomains") || entity.getClass().getName().equals("com.coretek.spte.Fighter"))
				{
					return !entity.getChildren().isEmpty();
				}

				return !entity.getLogicChildren().isEmpty();
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List)
			{
				List list = (List) inputElement;
				return list.toArray();
			}
			return new Object[0];
		}

	}

	public class ContentProvider2 implements ITreeContentProvider
	{

		private Object	parent;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object) <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object[] getChildren(Object parentElement)
		{
			this.parent = parentElement;
			if (parentElement instanceof Entity)
			{
				Entity entity = (Entity) parentElement;
				if (entity.getClass().getName().equals("com.coretek.spte.Fighter"))
				{
					List<Entity> entities = new ArrayList<Entity>();
					for (Entity child : entity.getChildren())
					{
						if (child.getClass().getName().equals("com.coretek.spte.Version"))
						{
							entities.add(child);
						}
					}
					return entities.toArray();
				}
				if (entity.getClass().getName().equals("com.coretek.spte.Version"))
				{
					List<Entity> entities = new ArrayList<Entity>();
					for (Entity child : entity.getChildren())
					{
						if (child.getClass().getName().equals("com.coretek.spte.FunctionDomains"))
						{
							entities.addAll(child.getChildren());
						}
					}
					return entities.toArray();
				}
				if (entity.getClass().getName().equals("com.coretek.spte.FunctionDomains"))
				{

					return entity.getChildren().toArray();
				}

				if (entity.getClass().getName().equals("com.coretek.spte.Topic"))
				{
					return entity.getChildren().toArray();
				}

				if (entity.getClass().getName().equals("com.coretek.spte.ContainedSignals"))
					return entity.getChildren().toArray();

				if (entity.getClass().getName().equals("com.coretek.spte.topic.ContainedSignals"))
					return entity.getChildren().toArray();

				if (entity.getClass().getName().equals("com.coretek.spte.FunctionNodeMsg"))
				{
					Field field = null;
					try
					{
						field = entity.getClass().getDeclaredField("topic");
					} catch (SecurityException e)
					{
						e.printStackTrace();
					} catch (NoSuchFieldException e)
					{
						e.printStackTrace();
					}
					field.setAccessible(true);
					Entity topic = null;
					try
					{
						topic = (Entity) field.get(entity);
					} catch (IllegalArgumentException e)
					{
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
					return new Object[] { topic };
				}

				return entity.getLogicChildren().toArray();
			}
			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object getParent(Object element)
		{
			if (element.getClass().getName().equals("com.coretek.spte.Topic"))
			{
				return this.parent;
			}
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;
				return entity.getLogicParent();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang
		 * .Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public boolean hasChildren(Object element)
		{
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;
				if (entity.getClass().getName().equals("com.coretek.spte.Version") || entity.getClass().getName().equals("com.coretek.spte.FunctionDomains") || entity.getClass().getName().equals("com.coretek.spte.Fighter"))
				{
					return !entity.getChildren().isEmpty();
				}

				if (entity.getClass().getName().equals("com.coretek.spte.Topic"))
				{
					return !entity.getChildren().isEmpty();
				}

				if (entity.getClass().getName().equals("com.coretek.spte.ContainedSignals"))
					return !entity.getChildren().isEmpty();

				if (entity.getClass().getName().equals("com.coretek.spte.topic.ContainedSignals"))
					return !entity.getChildren().isEmpty();

				if (entity.getClass().getName().equals("com.coretek.spte.FunctionNodeMsg"))
				{
					Field field = null;
					try
					{
						field = entity.getClass().getDeclaredField("topic");
					} catch (SecurityException e)
					{
						e.printStackTrace();
					} catch (NoSuchFieldException e)
					{
						e.printStackTrace();
					}
					field.setAccessible(true);
					Entity topic = null;
					try
					{
						topic = (Entity) field.get(entity);
					} catch (IllegalArgumentException e)
					{
						e.printStackTrace();
					} catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
					return topic != null;
				}

				return !entity.getLogicChildren().isEmpty();
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
		 */
		@Override
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List)
			{
				List list = (List) inputElement;
				return list.toArray();
			}
			return new Object[0];
		}

	}

	class TableLabelProvider2 implements ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			if (columnIndex == 7)
			{
				return "";
			}
			Entity entity = (Entity) element;
			Field[] fields = entity.getClass().getDeclaredFields();
			if (columnIndex >= fields.length)
			{
				return null;
			}
			Field field = fields[columnIndex];

			try
			{
				field.setAccessible(true);
				if (field.get(entity) == null)
					return "";
				return field.get(entity).toString();
			} catch (IllegalArgumentException e)
			{

				e.printStackTrace();
			} catch (IllegalAccessException e)
			{

				e.printStackTrace();
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-27
		 */
		@Override
		public void addListener(ILabelProviderListener listener)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose() <br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
		 * 2011-12-27
		 */
		@Override
		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener) <br/> <b>作者</b> 孙大巍 </br>
		 * <b>日期</b> 2011-12-27
		 */
		@Override
		public void removeListener(ILabelProviderListener listener)
		{

		}

	}

}
