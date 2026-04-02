package Typecheck.Pass;
import Typecheck.Types.*;
import Typecheck.SymbolTable.*;
import Typecheck.TypeCheckException;
import java.util.ArrayList;

// This pass implements the type rules.
// Some of the logic has been implemented for you in the Types.
// Check out the "canAccept" functions.
public class JudgementsPass extends ScopePass<Void> {
   public JudgementsPass(Scope s) {
      super(s);
   }


   //FOUNDATIONAL VISITS

   @Override
   public Void visitDecLit(Absyn.DecLit node) {
	   node.typeAnnotation = new INT();
	   return null;
   }

   @Override
   public Void visitStrLit(Absyn.StrLit node) {
	   node.typeAnnotation = new STRING();
	   return null;
   }

   @Override
   public Void visitID(Absyn.ID node) {
	   if (currentscope.hasVar(node.value)) {
		   node.typeAnnotation = currentscope.getVar(node.value).type;
	   } else if (currentscope.hasFun(node.value)) {
		   node.typeAnnotation = currentscope.getFun(node.value).returnType;
	   } else {
	   	throw new TypeCheckException("Undefined variable: " + node.value);
	   }

	   return null;
   }


   //RULE 13

   @Override
   public Void visitIfStmt(Absyn.IfStmt node) {	   
	   super.visitIfStmt(node);

	   Type expressionType = node.expression.typeAnnotation;
	   if (!(expressionType instanceof INT)) {
		   throw new TypeCheckException("If statement condition must be a number!");
	   }

	   return null;
   }

   @Override
   public Void visitWhileStmt(Absyn.WhileStmt node) {
	   super.visitWhileStmt(node);

	   Type expressionType = node.expression.typeAnnotation;
	   if (!(expressionType instanceof INT)) {
	   	throw new TypeCheckException("While statement condition must be a number!");
	   }

	   return null;
   }


   //RULE 8
   
   @Override
   public Void visitBinOp(Absyn.BinOp node) {
	   super.visitBinOp(node);

	   Type leftType = node.left.typeAnnotation;
	   Type rightType = node.right.typeAnnotation;
	   if (!(leftType instanceof INT)) {
		   throw new TypeCheckException("Left side of the binary operator must be a number!");
	   }
	   if (!(rightType instanceof INT)) {
		   throw new TypeCheckException("Right side of the binary operator must be a number!");
	   }

	   node.typeAnnotation = new INT();
	   return null;
   }


   //RULE 1
   
   @Override
   public Void visitVarDecl(Absyn.VarDecl node) {
	   super.visitVarDecl(node);

	   if (!(node.init instanceof Absyn.EmptyExp)) {
		Type declaredType = currentscope.getVar(node.name).type;	
	   	Type initializedType = node.init.typeAnnotation;
	   	if (!declaredType.canAccept(initializedType)) {
		   	throw new TypeCheckException("Initialized variable must match the declared variable type!");
	   	}
	   }
	   return null;

   }


   //RULE 2

   @Override
   public Void visitAssignExp(Absyn.AssignExp node) {
   	super.visitAssignExp(node);

	Type left = node.left.typeAnnotation;
	Type right = node.right.typeAnnotation;
	if (!(left.canAccept(right))) {
		throw new TypeCheckException("Assigned value must match the declared variable type!");
	}

	node.typeAnnotation = left;
	return null;
   }


   //RULE 14

   @Override
   public Void visitUnaryExp(Absyn.UnaryExp node) {
   	super.visitUnaryExp(node);

	Type expType = node.exp.typeAnnotation;

	if (node.prefix.equals("*")) {
		if (!(expType instanceof POINTER)) {
			throw new TypeCheckException("The * operator requires a pointer!");
		}
		node.typeAnnotation = ((POINTER) expType).type;
	} else if (node.prefix.equals("&")) {
		node.typeAnnotation = new POINTER(expType);
	} else {
		if (!(expType instanceof INT)) {
			throw new TypeCheckException("Unary operators require a number!");
		}
		node.typeAnnotation = new INT();
	}

	return null;
   }   

}
