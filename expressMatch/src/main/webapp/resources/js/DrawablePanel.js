/**
 * class will be used to control the states of the panel.
 */

function HandwritenPanel() {

	var ZERO = 0;
	var DRAWING = 1;
	var NOT_DRAWING = ZERO;
	var CONTEXT_2D = "2d";
	var BORDER = 20;
	var PANEL_MIN_SIZE_X = 215;
	var PANEL_MIN_SIZE_Y = 130;
	var id = 0;

	this.lastX = "";
	this.lastY = "";
	this.drawable = true;
	this.state = "";
	this.canvasName = "";
	this.context = "";
	this.canvasElement = "";
	this.strokes = [];
	this.historicalStrokes = [];

	this.zoon = function(h, v) {
		this.canvasElement.width = this.canvasElement.width + (BORDER * h) > PANEL_MIN_SIZE_X ? 
				this.canvasElement.width + (BORDER * h)
				: PANEL_MIN_SIZE_X;
		this.canvasElement.height = this.canvasElement.height + (BORDER * v) > PANEL_MIN_SIZE_Y ? 
				this.canvasElement.height + (BORDER * v)
				: PANEL_MIN_SIZE_Y;
		this.drawStrokes();
	};

	this.getStrokesBoundingBoxSize = function() {
		var leftCorner = this.getLeftTopCorner();
		var rightCorner = this.getRightBottonCorner();
		var res = {
			width : rightCorner.x - leftCorner.x,
			height : rightCorner.y - leftCorner.y
		};
		return res;
	};

	this.zoon = function(event) {
		var mousePosition = this.getNewMouseCordinates(event);
		mousePosition.x -= this.canvasElement.offsetLeft;
		mousePosition.y -= this.canvasElement.offsetTop;
		this.canvasElement.width = (mousePosition.x);
		this.canvasElement.height = (mousePosition.y + 80);
		
		if(this.canvasElement.width < PANEL_MIN_SIZE_X){
			this.canvasElement.width = PANEL_MIN_SIZE_X;
		}
		if(this.canvasElement.height < PANEL_MIN_SIZE_Y){
			this.canvasElement.height = PANEL_MIN_SIZE_Y;
		}
		
		this.drawStrokes();
	};

	this.init = function(_canvasName, _drawable, _jsonString) {
		this.lastX = ZERO;
		this.lastY = ZERO;
		this.strokes = this.fromJSONString(_jsonString);
		this.drawable = _drawable;
		this.state = NOT_DRAWING;
		this.canvasName = _canvasName;
		this.canvasElement = this.getCanvasElement(this.canvasName);
		this.context = this.getNew2DContext();
		this.registraEventos();
		this.startCanvas();
	};

	this.startCanvas = function() {
		if (this.strokes.length > 0) {
			if (this.drawable) {
				this.scaleStrokes();
				this.drawStrokes();
			} else {
				this.drawScaledStrokes(this.getExpressionScale());
			}
		}
	};

	this.scaleStrokes = function() {
		 var scale = this.getExpressionScale();
		 var leftCorner = this.getLeftTopCorner();
		 var bBox = this.getStrokesBoundingBoxSize();
		 var newExpressionSize = {width: (bBox.width*scale), height: (bBox.height*scale)}
		 bBox.width = (this.canvasElement.width - newExpressionSize.width)/2.0;
		 bBox.height = (this.canvasElement.height - newExpressionSize.height)/2.0;
		 leftCorner = {x: leftCorner.x *scale, y: leftCorner.y *scale}
		 bBox.width = (bBox.width - leftCorner.x);
		 bBox.height = (bBox.height - leftCorner.y);
		
		
		for ( var i = 0; i < this.strokes.length; i = i + 1) {
			for ( var j = 0; j < this.strokes[i].points.length; j =  j + 1){
				this.strokes[i].points[j].x = this.strokes[i].points[j].x * scale + bBox.width;
				this.strokes[i].points[j].y = this.strokes[i].points[j].y * scale + bBox.height;
			}
		}
	};
	
	this.scaleCanvas = function() {
		var rightCorner = this.getRightBottonCorner();
		if (rightCorner.x > this.canvasElement.width) {
			this.canvasElement.width = rightCorner.x + BORDER;
		}
		if (rightCorner.y > this.canvasElement.height) {
			this.canvasElement.height = rightCorner.y + BORDER;
		}
	};

	this.getExpressionScale = function() {
		var leftCorner = this.getLeftTopCorner();
		var rightCorner = this.getRightBottonCorner();
		
		var scales = {
			x : this.canvasElement.width
					/ (rightCorner.x - leftCorner.x + 2 * BORDER),
			y : this.canvasElement.height
					/ (rightCorner.y - leftCorner.y + 2 * BORDER)
		};

		if (scales.x < scales.y) {
			if (scales.x > 1.0) {
				scales.x = 1.0;
			}
			return scales.x;
		} else {
			if (scales.y > 1.0) {
				scales.y = 1.0;
			}
			return scales.y;
		}
		
	};

	this.getLeftTopCorner = function() {
		var leftTopCorner = {
			x : 500000,
			y : 500000
		};
		for ( var i = 0; i < this.strokes.length; i++) {
			for ( var j = 0; j < this.strokes[i].points.length; j++) {
				if (this.strokes[i].points[j].x < leftTopCorner.x) {
					leftTopCorner.x = this.strokes[i].points[j].x;
				}
				if (this.strokes[i].points[j].y < leftTopCorner.y) {
					leftTopCorner.y = this.strokes[i].points[j].y;
				}
			}
		}
		return leftTopCorner;
	};

	this.getRightBottonCorner = function() {
		var rightBottonCorner = {
			x : 0,
			y : 0
		};
		for ( var i = 0; i < this.strokes.length; i++) {
			for ( var j = 0; j < this.strokes[i].points.length; j++) {
				if (this.strokes[i].points[j].x > rightBottonCorner.x) {
					rightBottonCorner.x = this.strokes[i].points[j].x;
				}
				if (this.strokes[i].points[j].y > rightBottonCorner.y) {
					rightBottonCorner.y = this.strokes[i].points[j].y;
				}
			}
		}
		return rightBottonCorner;
	};

	this.resetCanvas = function() {
		this.strokes = [];
		this.clearCanvas();
	};

	this.asJSONString = function() {
		return JSON.stringify(this.strokes);
	};

	this.fromJSONString = function(jSonString) {
		if (0 != jSonString.length) {
			return JSON.parse(jSonString);
		}
		return [];
	};

	this.clearCanvas = function() {
		this.canvasElement.width = this.canvasElement.width;
		this.canvasElement.height = this.canvasElement.height;
		this.context.clearRect(ZERO, ZERO, this.canvasElement.width,
				this.canvasElement.height);
		this.context = this.getNew2DContext();
	};

	this.setXYValues = function(position) {
		this.lastX = position.x;
		this.lastY = position.y;
	};

	this.getNewMouseCordinates = function(event) {
		var clientRect = this.canvasElement.getBoundingClientRect();
		var mousePosition = {
			x : event.clientX - clientRect.left,
			y : event.clientY - clientRect.top
		};
		return mousePosition;
	};

	this.stopLine = function(event) {
		this.state = NOT_DRAWING;
	};

	this.beginLine = function(event) {
		id = id + 1;
		var mousePosition = this.getNewMouseCordinates(event);

		this.clearHistoricalStrokes();
		this.state = DRAWING;
		this.setXYValues(mousePosition);

		this.currentStroke = new Stroke(id);
		this.currentStroke.points.push(mousePosition);
		this.strokes.push(this.currentStroke);

	};

	this.get2DContext = function() {
		return this.canvasElement.getContext(CONTEXT_2D);
	};

	this.getNew2DContext = function() {
		var cxt = this.get2DContext();
		cxt.lineWidth = 2;
		cxt.strokeStyle = 'gray';
		cxt.lineCap = "round";
		return cxt;
	};

	this.getCanvasElement = function(canvasName) {
		return document.getElementById(canvasName);
	};

	this.drawScaledStrokes = function(scale) {
		 this.clearCanvas();
		 var scale = this.getExpressionScale();
		 var leftCorner = this.getLeftTopCorner();
		 var bBox = this.getStrokesBoundingBoxSize();
		 var newExpressionSize = {width: (bBox.width*scale), height: (bBox.height*scale)}
		 bBox.width = (this.canvasElement.width - newExpressionSize.width)/2.0;
		 bBox.height = (this.canvasElement.height - newExpressionSize.height)/2.0;
		 leftCorner = {x: leftCorner.x *scale, y: leftCorner.y *scale}
		 bBox.width = (bBox.width - leftCorner.x);
		 bBox.height = (bBox.height - leftCorner.y);
		
		
		for ( var i = 0; i < this.strokes.length; i = i + 1) {
			this.drawStroke(this.strokes[i], scale, bBox.width , bBox.height);
		}
	};

	this.drawStrokes = function() {
		this.clearCanvas();
		for ( var i = 0; i < this.strokes.length; i = i + 1) {
			this.drawStroke(this.strokes[i], 1.0, 0.0, 0.0);
		}
	};

	this.drawStroke = function(stroke, scale, xOff, yOff) {
		this.setXYValues(stroke.points[0]);
		this.context.beginPath();
		for ( var i = 1; i < stroke.points.length; i = i + 1) {
			
			this.context.moveTo((this.lastX * scale) + xOff, (this.lastY * scale) + yOff);
			this.context.lineTo((stroke.points[i].x * scale) + xOff, (stroke.points[i].y * scale) + yOff);
			this.setXYValues(stroke.points[i]);
			
//			alert(""+ ((stroke.points[i].x * scale) + xOff) + ((stroke.points[i].y * scale) + yOff) + scale + xOff + yOff));
			
		}
		this.context.stroke();
	};

	this.continueLine = function(event) {
		if (this.state == DRAWING) {
			var mousePosition = this.getNewMouseCordinates(event);
			if (!this.isSamePoint(mousePosition)) {
				this.currentStroke.points.push(mousePosition);
				this.drawLineSegment(mousePosition);
			}
		}
	};

	this.drawLineSegment = function(mousePosition) {
		this.context.beginPath();
		this.context.moveTo(this.lastX, this.lastY);
		this.context.lineTo(mousePosition.x, mousePosition.y);
		this.context.stroke();
		this.setXYValues(mousePosition);
	};

	this.endLine = function(event) {
		this.continueLine(event);
		this.state = NOT_DRAWING;
	};

	this.isSamePoint = function(mousePosition) {
		return (this.lastX == mousePosition.x && this.lastY == mousePosition.y);
	};

	this.undo = function() {
		if (this.strokes.length > 0) {
			this.historicalStrokes.push(this.strokes.pop());
			this.drawStrokes();
		}
	};

	this.redo = function() {
		if (this.historicalStrokes.length > 0) {
			this.strokes.push(this.historicalStrokes.pop());
			this.drawStrokes();
		}
	};

	this.clearHistoricalStrokes = function() {
		this.historicalStrokes.length = ZERO;
	};

	this.registraEventos = function() {
		if (this.drawable) {
			var handwritenPanel = this;
			this.canvasElement.addEventListener('mousemove', function(e) {
				handwritenPanel.continueLine(e);
			}, false);
			this.canvasElement.addEventListener('mousedown', function(e) {
				handwritenPanel.beginLine(e);
			}, false);
			this.canvasElement.addEventListener('mouseup', function(e) {
				handwritenPanel.endLine(e);
			}, false);
			this.canvasElement.addEventListener('mouseover', function(e) {
				handwritenPanel.stopLine();
			}, false);
			this.canvasElement.addEventListener('touchmove', function(e) {
				handwritenPanel.continueLine(e);
			}, false);
			this.canvasElement.addEventListener('touchstart', function(e) {
				handwritenPanel.beginLine(e);
			}, false);
			this.canvasElement.addEventListener('touchend', function(e) {
				handwritenPanel.endLine(e);
			}, false);
			this.canvasElement.addEventListener('touchleave', function(e) {
				handwritenPanel.stopLine();
			}, false);
		}
	};
};

function Stroke(identifier) {
	this.id = identifier;
	this.time = new Date();
	this.time = this.time.setHours(this.time.getHours()
			- this.time.getTimezoneOffset() / 60);
	this.points = [];
};
