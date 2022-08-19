/**
 * Validação de formulario
 */
 function validar(){
//	alert('teste')
	let nome = frmContato.nome.value
	let fone = frmContato.fone.value
	if (nome === ""){
		alert ("Preencha o campo Nome")
		frmContato.nome.focus() //reposiciona o cursor para o campo nome
		return false
	}else if (fone === ""){
		alert ("Preencha o campo Fone")
		frmContato.fone.focus()
		return false
	}else{
		document.forms["frmContato"].submit()
	}

	
}