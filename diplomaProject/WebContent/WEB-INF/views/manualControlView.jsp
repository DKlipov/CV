<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<button id="manual-mode">Ручной режим</button>
<div id="manual" hidden="true">
	<p>Скорость:
	<span id="manualControlSpeed">100</span>
	</p>
	<p>Поворот:
	<span id="manualControlTurn">0</span>
	</p>

	<p>
		<button data-move="speedDown" id='81'>Q</button>
		<button data-move="front" id='87'>W</button>
		<button data-move="speedUp" id='69'>E</button>
	</p>
	<p>
		<button data-move="left" id='65'>A</button>
		<button data-move="back" id='83'>S</button>
		<button data-move="right" id='68'>D</button>
	</p>
</div>
