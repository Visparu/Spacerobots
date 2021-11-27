package de.visparu.spacerobots.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.game.entities.external.Robot;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.ClassFinder;
import de.visparu.spacerobots.util.GraphicsHelper;
import de.visparu.spacerobots.util.Vector2D;

public final class MainMenu implements MouseSubscriber
{
	private final class RobotNameComparator implements Comparator<InternalRobot>
	{
		@Override
		public int compare(final InternalRobot o1, final InternalRobot o2)
		{
			return o1.getName().compareTo(o2.getName());
		}
	}
	
	private static final int SEED = 1337;
	
	public static final boolean USE_SEED = false;
	
	private InternalArena       arena;
	private List<InternalRobot> availableRobots;
	private List<InternalRobot> selectedRobots;
	
	private double          slotRobotHeading;
	private Rectangle       leftPanel;
	private Rectangle       rightPanel;
	private List<Rectangle> leftSlots;
	private List<Rectangle> rightSlots;
	
	private Rectangle startButton;
	private Ellipse2D escapeControl;
	private Ellipse2D resetControl;
	
	private Ellipse2D pauseControl;
	private boolean   gameRunning;
	private boolean   gamePreview;
	
	private boolean gamePaused;
	
	private boolean initialized;
	
	public MainMenu()
	{
		this.initialized = false;
	}
	
	public void init(final InternalArena arena)
	{
		final var canvasBounds      = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		final var arenaOrigin       = GraphicsInfo.ARENA_ORIGIN;
		final var panelMargin       = GraphicsInfo.MENU_PANEL_SIZE_SPACER_MARGIN;
		final var panelSpacerWidth  = GraphicsInfo.MENU_PANEL_SIZE_SPACER_CONTENT;
		final var startButtonHeight = GraphicsInfo.MENU_START_HEIGHT;
		final var slotSize          = GraphicsInfo.MENU_ROBOT_PREVIEW_SLOT_SIZE;
		final var fontSize          = GraphicsInfo.MENU_ROBOT_PREVIEW_TEXT_SIZE;
		final var slotDistanceVert  = GraphicsInfo.MENU_ROBOT_PREVIEW_TEXT_DISTANCE;
		final var controlDistance   = GraphicsInfo.MENU_CONTROLS_DISTANCE;
		final var plain             = Font.PLAIN;
		final var argbType          = BufferedImage.TYPE_INT_ARGB;
		
		if (this.initialized)
		{
			return;
		}
		
		this.arena            = arena;
		this.availableRobots  = new ArrayList<>();
		this.selectedRobots   = new ArrayList<>();
		this.leftSlots        = new ArrayList<>();
		this.rightSlots       = new ArrayList<>();
		this.slotRobotHeading = 90;
		this.gameRunning      = false;
		this.gamePreview      = true;
		
		final var robotClasses = ClassFinder.find("de.visparu.spacerobots.game.entities.external.robots");
		for (var i = 0; i < robotClasses.size(); i++)
		{
			try
			{
				final var robot  = (Robot) robotClasses.get(i).getConstructor().newInstance();
				final var iRobot = new InternalRobot();
				iRobot.init(robot, new Vector2D(0, 0), this.slotRobotHeading);
				this.availableRobots.add(iRobot);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		this.availableRobots.sort(new RobotNameComparator());
		
		final var canvasWidth  = (int) canvasBounds.getWidth();
		final var canvasHeight = (int) canvasBounds.getHeight();
		
		final var panelWidth  = (canvasWidth - (2 * panelMargin) - panelSpacerWidth) / 2;
		final var panelHeight = (canvasHeight - (2 * panelMargin));
		
		final var leftPanelX = panelMargin;
		final var leftPanelY = panelMargin;
		
		final var rightPanelX = panelMargin + panelWidth + panelSpacerWidth;
		final var rightPanelY = panelMargin;
		
		final var rightPanelHeight = panelHeight - startButtonHeight - panelSpacerWidth;
		
		this.leftPanel  = new Rectangle(leftPanelX, leftPanelY, panelWidth, panelHeight);
		this.rightPanel = new Rectangle(rightPanelX, rightPanelY, panelWidth, rightPanelHeight);
		
		final var font       = new Font("Times New Roman", plain, fontSize);
		final var metrics    = new BufferedImage(1, 1, argbType).getGraphics().getFontMetrics(font);
		final var nameHeight = metrics.getAscent() + metrics.getHeight();
		
		final var slotsPerRow         = panelWidth / slotSize;
		final var slotsPerColumnLeft  = panelHeight / (slotSize + slotDistanceVert + nameHeight);
		final var slotsPerColumnRight = rightPanelHeight / (slotSize + slotDistanceVert + nameHeight);
		
		for (var i = 0; i < (slotsPerRow * slotsPerColumnLeft); i++)
		{
			final var slotX = i % slotsPerRow;
			final var slotY = i / slotsPerRow;
			
			final var slotSpacing = (panelWidth - (slotsPerRow * slotSize)) / (slotsPerRow + 1);
			
			final var absX = leftPanelX + slotSpacing + (slotX * (slotSize + slotSpacing));
			final var absY = leftPanelY + slotSpacing + (slotY * (slotSize + nameHeight + slotDistanceVert));
			
			this.leftSlots.add(new Rectangle(absX, absY, slotSize, slotSize));
			if (i < (slotsPerRow * slotsPerColumnRight))
			{
				this.rightSlots.add(new Rectangle(absX + panelWidth + panelSpacerWidth, absY, slotSize, slotSize));
			}
		}
		
		final var startX      = rightPanelX;
		final var startY      = rightPanelY + rightPanelHeight + panelSpacerWidth;
		final var startWidth  = panelWidth;
		final var startHeight = startButtonHeight;
		
		this.startButton = new Rectangle(startX, startY, startWidth, startHeight);
		
		final var arenaMarginX = arenaOrigin.getX();
		final var arenaMarginY = arenaOrigin.getY();
		
		final var    escapeControlULx = arenaMarginX;
		final double escapeControlULy = controlDistance;
		final var    controlSize      = arenaMarginY - (2 * controlDistance);
		
		final var controlSizeComplete = controlSize + (2 * controlDistance);
		
		this.escapeControl = new Ellipse2D.Double(escapeControlULx, escapeControlULy, controlSize, controlSize);
		this.resetControl  = new Ellipse2D.Double(escapeControlULx + controlSizeComplete, escapeControlULy, controlSize, controlSize);
		this.pauseControl  = new Ellipse2D.Double(escapeControlULx + (2 * controlSizeComplete), escapeControlULy, controlSize, controlSize);
		
		this.initialized = true;
	}
	
	@Override
	public void mouseMoved(final MouseEvent e)
	{
		
	}
	
	@Override
	public void mousePressed(final MouseEvent e)
	{
		final var     x          = e.getX();
		final var     y          = e.getY();
		final Point2D mousePoint = new Point2D.Double(x, y);
		if (!this.gameRunning)
		{
			for (var i = 0; i < this.leftSlots.size(); i++)
			{
				if (this.leftSlots.get(i).contains(mousePoint))
				{
					if (this.availableRobots.size() > i)
					{
						this.selectedRobots.add(this.availableRobots.get(i));
					}
					break;
				}
			}
			for (var i = 0; i < this.rightSlots.size(); i++)
			{
				if (this.rightSlots.get(i).contains(mousePoint))
				{
					if (this.selectedRobots.size() > i)
					{
						this.selectedRobots.remove(i);
					}
					break;
				}
			}
			if (this.startButton.contains(mousePoint) && !this.selectedRobots.isEmpty())
			{
				this.startNewSimulation();
			}
		}
		else
		{
			if (this.escapeControl.contains(mousePoint))
			{
				this.gameRunning = false;
				this.selectedRobots.clear();
				this.gamePaused = false;
			}
			if (this.resetControl.contains(mousePoint))
			{
				this.startNewSimulation();
			}
			if (this.pauseControl.contains(mousePoint))
			{
				this.gamePaused = !this.gamePaused;
			}
		}
	}
	
	@Override
	public void mouseReleased(final MouseEvent e)
	{
		
	}
	
	public void render(final Graphics2D g2d)
	{
		this.arena.render(g2d, this.gameRunning);
		
		if (!this.gameRunning)
		{
			this.renderMenu(g2d);
		}
		else
		{
			this.renderControls(g2d);
			this.renderInformation(g2d);
		}
	}
	
	private void renderControls(final Graphics2D g2d)
	{
		final var argbType = BufferedImage.TYPE_INT_ARGB;
		
		final var controlSize = this.escapeControl.getWidth();
		
		final var escapeImage = new BufferedImage((int) controlSize, (int) controlSize, argbType);
		final var resetImage  = new BufferedImage((int) controlSize, (int) controlSize, argbType);
		final var pauseImage  = new BufferedImage((int) controlSize, (int) controlSize, argbType);
		
		final var g2dEscape = escapeImage.createGraphics();
		final var g2dReset  = resetImage.createGraphics();
		final var g2dPause  = pauseImage.createGraphics();
		
		this.renderControlsEscape(g2dEscape, (int) controlSize);
		this.renderControlsReset(g2dReset, (int) controlSize);
		this.renderControlsPause(g2dPause, (int) controlSize);
		
		g2d.drawImage(escapeImage, (int) this.escapeControl.getX(), (int) this.escapeControl.getY(), null);
		g2d.drawImage(resetImage, (int) this.resetControl.getX(), (int) this.resetControl.getY(), null);
		g2d.drawImage(pauseImage, (int) this.pauseControl.getX(), (int) this.pauseControl.getY(), null);
		
		g2dEscape.dispose();
		g2dReset.dispose();
		g2dPause.dispose();
	}
	
	private void renderControlsBackground(final Graphics2D g2dControl, final int controlWidth)
	{
		final var controlBackground = GraphicsInfo.MENU_START_INNER;
		final var controlBorder     = GraphicsInfo.MENU_START_OUTER;
		final var borderWidth       = GraphicsInfo.MENU_CONTROLS_BORDER_WIDTH;
		
		g2dControl.setColor(controlBackground);
		g2dControl.fillOval(borderWidth / 2, borderWidth / 2, controlWidth - borderWidth, controlWidth - borderWidth);
		
		g2dControl.setColor(controlBorder);
		g2dControl.setStroke(new BasicStroke(borderWidth));
		g2dControl.drawOval(borderWidth / 2, borderWidth / 2, controlWidth - borderWidth, controlWidth - borderWidth);
	}
	
	private void renderControlsEscape(final Graphics2D g2dEscape, final int controlWidth)
	{
		final var controlBorder = GraphicsInfo.MENU_START_OUTER.darker();
		final var strokeWidth   = 4F;
		
		this.renderControlsBackground(g2dEscape, controlWidth);
		
		final var closer  = controlWidth / 3;
		final var further = ((controlWidth / 3) * 2) + 1;
		
		g2dEscape.setColor(controlBorder);
		g2dEscape.setStroke(new BasicStroke(strokeWidth));
		g2dEscape.drawLine(closer, closer, further, further);
		g2dEscape.drawLine(closer, further, further, closer);
	}
	
	private void renderControlsPause(final Graphics2D g2dPause, final int controlWidth)
	{
		final var controlBorder = GraphicsInfo.MENU_START_OUTER.darker();
		final var strokeWidth   = 4F;
		
		this.renderControlsBackground(g2dPause, controlWidth);
		
		if (this.gamePaused)
		{
			final var arrowX = new int[3];
			final var arrowY = new int[3];
			
			final var lX = (controlWidth / 7) * 3;
			final var rX = (controlWidth / 7) * 6;
			final var uY = (int) ((controlWidth / 7.0F) * 2.5F);
			final var mY = (controlWidth / 7) * 4;
			final var lY = (int) ((controlWidth / 7.0F) * 5.5F);
			
			arrowX[0] = lX;
			arrowX[1] = lX;
			arrowX[2] = rX;
			
			arrowY[0] = uY;
			arrowY[1] = lY;
			arrowY[2] = mY;
			
			g2dPause.setColor(controlBorder);
			g2dPause.fillPolygon(arrowX, arrowY, 3);
		}
		else
		{
			final var lX = (controlWidth / 5) * 2;
			final var rX = ((controlWidth / 5) * 3) + 1;
			final var uY = controlWidth / 3;
			final var lY = ((controlWidth / 3) * 2) + 1;
			
			g2dPause.setColor(controlBorder);
			g2dPause.setStroke(new BasicStroke(strokeWidth));
			g2dPause.drawLine(lX, uY, lX, lY);
			g2dPause.drawLine(rX, uY, rX, lY);
		}
	}
	
	private void renderControlsReset(final Graphics2D g2dReset, final int controlWidth)
	{
		final var controlBorder = GraphicsInfo.MENU_START_OUTER.darker();
		final var strokeWidth   = 3F;
		
		this.renderControlsBackground(g2dReset, controlWidth);
		
		final var closer  = controlWidth / 4;
		final var further = (controlWidth / 4) * 3;
		
		g2dReset.setColor(controlBorder);
		g2dReset.setStroke(new BasicStroke(strokeWidth));
		g2dReset.drawArc(closer, closer, further - closer, further - closer, 45, 270);
		
		final double centerX        = controlWidth / 2.0F;
		final double centerY        = controlWidth / 2.0F;
		final double strokeDistance = ((controlWidth / 4.0F) * 2.0F) / 2.0F;
		final var    arrowCenterX   = centerX + (Math.cos(Math.toRadians(45)) * strokeDistance);
		final var    arrowCenterY   = centerY + (Math.sin(Math.toRadians(45)) * strokeDistance);
		
		final var arrowX = new int[3];
		final var arrowY = new int[3];
		
		final var arrowHeading = 315D;
		final var arrowSize    = 7D;
		for (var i = 0; i < 3; i++)
		{
			final var heading = arrowHeading + (i * (360.0F / 3.0F));
			arrowX[i] = (int) (arrowCenterX + (Math.cos(Math.toRadians(heading)) * arrowSize));
			arrowY[i] = (int) (arrowCenterY + (Math.sin(Math.toRadians(heading)) * arrowSize));
		}
		
		g2dReset.fillPolygon(arrowX, arrowY, 3);
	}
	
	private void renderInformation(final Graphics2D g2d)
	{
		final var arenaOrigin            = GraphicsInfo.ARENA_ORIGIN;
		final var arenaBounds            = GameInfo.ARENA_BOUNDS;
		final var canvasBounds           = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		final var barWidth               = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_WIDTH;
		final var barBackgroundIdle      = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_IDLE;
		final var barBackgroundWarning   = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_WARNING;
		final var barBackgroundCritical  = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_CRITICAL;
		final var barBackgroundCountdown = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_COUNTDOWN;
		final var barBackgroundSupernova = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_SUPERNOVA;
		final var barStroke              = GraphicsInfo.MENU_INFORMATION_SUDDEN_DEATH_TIMER_BORDER;
		final var normalPlayDelimiter    = GameInfo.NORMAL_PLAY_DELIMITER;
		final var suddenDeathDelimiter   = GameInfo.SUDDEN_DEATH_DELIMITER;
		final var supernovaDelimiter     = GameInfo.TIMEOUT_DELIMITER;
		final var preIteration           = GraphicsInfo.ARENA_COUNTDOWN_TIME;
		
		final var marginHeight = (canvasBounds.height - arenaBounds.height) / 2;
		final var barHeight    = marginHeight - (15 * 2);
		final var x            = (int) arenaOrigin.getX();
		final var y            = (int) arenaOrigin.getY() + arenaBounds.height + ((marginHeight - barHeight) / 2);
		
		final var standardFillWidth  = Math.max(Math.min((int) (barWidth * ((double) this.arena.getIteration() / (double) normalPlayDelimiter)), barWidth), 0);
		final var criticalFillWidth  = Math.max(Math.min((int) (barWidth * (this.arena.getSuddenDeathSeverity() / suddenDeathDelimiter)), barWidth), 0);
		final var countdownFillWidth = Math.max((int) (barWidth * ((double) this.arena.getIteration() / (double) -preIteration)), 0);
		final var supernovaFillWidth = Math
			.max(Math.min((int) (barWidth * ((double) (this.arena.getIteration() - normalPlayDelimiter - suddenDeathDelimiter) / (double) supernovaDelimiter)), barWidth), 0);
		
		g2d.setColor(barBackgroundIdle);
		g2d.fillRect(x, y, standardFillWidth, barHeight);
		
		final var criticalityColorGradient = this.arena.getSuddenDeathSeverity() / suddenDeathDelimiter;
		final var red                      = barBackgroundCritical.getRed();
		final var green                    = Math.min(Math.max((int) (barBackgroundWarning.getGreen() * (1 - criticalityColorGradient)), 0), 255);
		final var criticalityColor         = new Color(red, green, 0);
		
		g2d.setColor(criticalityColor);
		g2d.fillRect(x, y, criticalFillWidth, barHeight);
		
		g2d.setColor(barBackgroundCountdown);
		g2d.fillRect(x, y, countdownFillWidth, barHeight);
		
		g2d.setColor(barBackgroundSupernova);
		g2d.fillRect(x, y, supernovaFillWidth, barHeight);
		
		g2d.setColor(barStroke);
		g2d.drawRect(x, y, barWidth, barHeight);
	}
	
	private void renderMenu(final Graphics2D g2d)
	{
		final var canvasBounds = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		
		final var canvasWidth  = (int) canvasBounds.getWidth();
		final var canvasHeight = (int) canvasBounds.getHeight();
		
		this.renderMenuBackground(g2d, canvasWidth, canvasHeight);
		this.renderMenuPanels(g2d);
		this.renderMenuStartButton(g2d);
	}
	
	private void renderMenuBackground(final Graphics2D g2d, final int canvasWidth, final int canvasHeight)
	{
		final var menuBackgroundColor = GraphicsInfo.MENU_BACKGROUND;
		
		g2d.setColor(menuBackgroundColor);
		g2d.fillRect(0, 0, canvasWidth, canvasHeight);
	}
	
	private void renderMenuPanels(final Graphics2D g2d)
	{
		final var argbType = BufferedImage.TYPE_INT_ARGB;
		
		final var panelWidth       = (int) this.leftPanel.getWidth();
		final var panelHeightLeft  = (int) this.leftPanel.getHeight();
		final var panelHeightRight = (int) this.rightPanel.getHeight();
		final var leftPanelImage   = new BufferedImage(panelWidth, panelHeightLeft, argbType);
		final var rightPanelImage  = new BufferedImage(panelWidth, panelHeightRight, argbType);
		final var g2dLeftPanel     = leftPanelImage.createGraphics();
		final var g2dRightPanel    = rightPanelImage.createGraphics();
		
		this.renderMenuPanelsLeft(g2dLeftPanel, panelWidth, panelHeightLeft);
		this.renderMenuPanelsRight(g2dRightPanel, panelWidth, panelHeightRight);
		
		g2d.drawImage(leftPanelImage, (int) this.leftPanel.getX(), (int) this.leftPanel.getY(), panelWidth, panelHeightLeft, null);
		g2d.drawImage(rightPanelImage, (int) this.rightPanel.getX(), (int) this.rightPanel.getY(), panelWidth, panelHeightRight, null);
		
		g2dLeftPanel.dispose();
		g2dRightPanel.dispose();
	}
	
	private void renderMenuPanelsLeft(final Graphics2D g2dLeftPanel, final int panelWidth, final int panelHeight)
	{
		final var panelBackgroundColor = GraphicsInfo.MENU_PANEL_COLOR_BACKGROUND;
		final var panelBorderColor     = GraphicsInfo.MENU_PANEL_COLOR_BORDER;
		
		g2dLeftPanel.setColor(panelBackgroundColor);
		g2dLeftPanel.fillRect(0, 0, panelWidth, panelHeight);
		
		g2dLeftPanel.setColor(panelBorderColor);
		g2dLeftPanel.drawRect(0, 0, panelWidth - 1, panelHeight - 1);
		
		this.renderMenuSlots(g2dLeftPanel, panelWidth, this.availableRobots);
	}
	
	private void renderMenuPanelsRight(final Graphics2D g2dRightPanel, final int panelWidth, final int panelHeight)
	{
		final var panelBackgroundColor = GraphicsInfo.MENU_PANEL_COLOR_BACKGROUND;
		final var panelBorderColor     = GraphicsInfo.MENU_PANEL_COLOR_BORDER;
		
		g2dRightPanel.setColor(panelBackgroundColor);
		g2dRightPanel.fillRect(0, 0, panelWidth, panelHeight);
		
		g2dRightPanel.setColor(panelBorderColor);
		g2dRightPanel.drawRect(0, 0, panelWidth - 1, panelHeight - 1);
		
		this.renderMenuSlots(g2dRightPanel, panelWidth, this.selectedRobots);
	}
	
	private void renderMenuSlots(final Graphics2D g2dPanel, final int panelWidth, final List<InternalRobot> robots)
	{
		final var slotWidth        = GraphicsInfo.MENU_ROBOT_PREVIEW_SLOT_SIZE;
		final var slotHeight       = GraphicsInfo.MENU_ROBOT_PREVIEW_SLOT_SIZE;
		final var slotDistanceVert = GraphicsInfo.MENU_ROBOT_PREVIEW_TEXT_DISTANCE;
		final var fontSize         = GraphicsInfo.MENU_ROBOT_PREVIEW_TEXT_SIZE;
		final var slotTextColor    = GraphicsInfo.MENU_ROBOT_PREVIEW_TEXT_COLOR;
		final var plain            = Font.PLAIN;
		final var argbType         = BufferedImage.TYPE_INT_ARGB;
		
		final var slotsPerRow = panelWidth / slotWidth;
		
		final var font       = new Font("Times New Roman", plain, fontSize);
		final var metrics    = g2dPanel.getFontMetrics(font);
		final var nameHeight = metrics.getAscent() + metrics.getHeight();
		
		for (var i = 0; i < robots.size(); i++)
		{
			final var slotX = i % slotsPerRow;
			final var slotY = i / slotsPerRow;
			
			final var slotSpacing = (panelWidth - (slotsPerRow * slotWidth)) / (slotsPerRow + 1);
			
			final var absX = slotSpacing + (slotX * (slotWidth + slotSpacing));
			final var absY = slotSpacing + (slotY * (slotHeight + nameHeight + slotDistanceVert));
			
			final var slotImage = new BufferedImage(slotWidth, slotHeight, argbType);
			final var g2dSlot  = slotImage.createGraphics();
			this.renderMenuSlotsSingle(g2dSlot, slotWidth, slotHeight, robots.get(i));
			
			if (robots.size() >= i)
			{
				final var robotName = robots.get(i).getName();
				final var textX     = absX + (slotWidth / 2);
				final var textY     = absY + slotHeight + (nameHeight / 2);
				GraphicsHelper.renderCenteredText(g2dPanel, robotName, font, textX, textY, slotTextColor);
				
				g2dPanel.drawImage(slotImage, absX, absY, slotWidth, slotHeight, null);
			}
			
			g2dSlot.dispose();
		}
	}
	
	private void renderMenuSlotsSingle(final Graphics2D g2dSlot, final int slotWidth, final int slotHeight, final InternalRobot robot)
	{
		final var slotBackgroundColor = GraphicsInfo.MENU_ROBOT_PREVIEW_COLOR_BACKGROUND;
		final var slotBorderColor     = GraphicsInfo.MENU_ROBOT_PREVIEW_COLOR_BORDER;
		
		g2dSlot.setColor(slotBackgroundColor);
		g2dSlot.fillRect(0, 0, slotWidth, slotHeight);
		
		g2dSlot.translate(slotWidth / 2, slotHeight / 2);
		robot.render(g2dSlot, false, false);
		
		g2dSlot.translate(-(slotWidth / 2), -(slotHeight / 2));
		g2dSlot.setColor(slotBorderColor);
		g2dSlot.drawRect(0, 0, slotWidth - 1, slotHeight - 1);
	}
	
	private void renderMenuStartButton(final Graphics2D g2d)
	{
		final var outerColor = GraphicsInfo.MENU_START_OUTER;
		final var innerColor = GraphicsInfo.MENU_START_INNER;
		final var textColor  = GraphicsInfo.MENU_START_TEXT_COLOR;
		final var textSize   = GraphicsInfo.MENU_START_TEXT_SIZE;
		final var margin     = GraphicsInfo.MENU_START_MARGIN;
		final var bold       = Font.BOLD;
		
		g2d.setColor(outerColor);
		g2d.fill(this.startButton);
		
		final var startInnerX      = this.startButton.x + margin;
		final var startInnerY      = this.startButton.y + margin;
		final var startInnerWidth  = this.startButton.width - (2 * margin);
		final var startInnerHeight = this.startButton.height - (2 * margin);
		
		g2d.setColor(innerColor);
		g2d.fillRect(startInnerX, startInnerY, startInnerWidth, startInnerHeight);
		
		final var startCenterX = (int) this.startButton.getCenterX();
		final var startCenterY = (int) this.startButton.getCenterY();
		final var font         = new Font("Times New Roman", bold, textSize);
		GraphicsHelper.renderCenteredText(g2d, "Start", font, startCenterX, startCenterY, textColor);
	}
	
	public void startNewSimulation()
	{
		final var countdownTime = GraphicsInfo.ARENA_COUNTDOWN_TIME;
		
		this.gameRunning = true;
		this.gamePaused  = false;
		this.arena.reset();
		this.arena.init(ClassFinder.getClassesFromList(this.selectedRobots), countdownTime, MainMenu.SEED);
	}
	
	public void update()
	{
		final var countdownTime = GraphicsInfo.ARENA_COUNTDOWN_TIME;
		final var spinSpeed     = GraphicsInfo.MENU_ROBOT_PREVIEW_SPIN_SPEED;
		
		if ((!this.gameRunning && this.gamePreview) && (this.arena.getRobotCount() <= 1))
		{
			this.arena.reset();
			this.arena.init(ClassFinder.getClassesFromList(this.availableRobots), -countdownTime, (int) (Math.random() * Integer.MAX_VALUE));
		}
		if ((this.gameRunning || this.gamePreview) && !this.gamePaused)
		{
			this.arena.update();
		}
		this.slotRobotHeading += spinSpeed;
		for (final InternalRobot element : this.availableRobots)
		{
			element.setHeading(this.slotRobotHeading);
		}
	}
}
