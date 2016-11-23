var $dialogBox;
function showDialog(caption, content, button1, button2, button1func, button2func)
{
	if(!$dialogBox){
		var html='<div id="divDialogBox" style="position: absolute;display:none;top:0px;z-index: 200;background:rgba(0,0,0,0.5);width:100%;height:100%">'+
'<div style="border-radius: 3px;box-shadow: 0px 2px 6px #000000;position: absolute;width:300px;height:220px;background:#f7f7f7;left:50%;top:50%;margin-left:-150px;margin-top:-130px">'+
'<table width="100%" height="100%" cellpadding="0" cellspacing="0">'+
'<tr height="60">'+
'<td style="border-bottom:2px solid #2E75B6" align="center">'+
'<span class="boxheader"><span id="dialogCaption">Dialog Caption</span></span>'+
'</td>'+
'</tr>'+
'<tr>'+
'<td align="center" id="dialogContent" style="padding:10px">'+
'	Dialog Content'+
'</td>'+
'</tr>'+
'<tr>'+
'<td height="50" style="border-top:1px solid #bbbbbb;" align="center">'+
'	<table height="100%" width="100%" cellpadding="0" cellspacing="0">'+
'	<tr>'+
'		<td align="center" style="border-right:1px solid #bbbbbb" id="dialogButton1">'+
'		Button1'+
'		</td>'+
'		<td width="50%" align="center" id="dialogButton2">'+
'		Button2'+
'		</td>'+
'	</tr>'+
'	</table>'+
'</td>'+
'</tr>'+
'</table>'+
'</div>'+
'</div>';
		$dialogBox = $(html).appendTo('body');
	}
	$("#dialogCaption").html(caption);
	$("#dialogContent").html(content);
	$("#dialogButton1").html(button1);
	if(button2=="" || button2==null)
	{
		$("#dialogButton2").hide();
	}
	else
	{
		$("#dialogButton2").show();
		$("#dialogButton2").html(button2);
	}
	if(button1func)
		$("#dialogButton1").unbind("click").click(button1func);
	else 
		$("#dialogButton1").unbind("click").click(function(){hideDialog();});
	if(button2func)
		$("#dialogButton2").unbind("click").click(button2func);
	else
		$("#dialogButton2").unbind("click").click(function(){hideDialog();});
	$dialogBox.show();
}
function hideDialog()
{
	$dialogBox.hide();
}
function messageBox(message)
{
	showDialog("Message",message,"OK");
}
