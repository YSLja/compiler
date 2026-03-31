package Typecheck.Pass;
import Typecheck.Types.*;
import Typecheck.SymbolTable.*;
import Typecheck.TypeCheckException;
import java.util.ArrayList;

import Absyn.Decl;

public class FunAndVarScopePass extends ScopePass<Void> {

   public FunAndVarScopePass(Scope s) {
      super(s);
   }
// Hint: Parameters behave like variables inside the function scope.
// 1. Ensure no function with this name already exists in the current scope.
// 2. Add the parameter as a variable symbol.
// 3. Use the parameter's typeAnnotation as its type.
   @Override
	public Void visitParameter(Absyn.Parameter node) {

       // 1. Ensure no function with this name already exists in the current scope.
       if (this.currentscope.hasLocalFun(node.name)) {
           throw new TypeCheckException("Tried to define var ("+node.name+") but fun with same name already exists");
       }
       // 2. Add the parameter as a variable symbol.
       // 3. Use the parameter's typeAnnotation as its type.
       this.currentscope.addVar(node.name, new VarSymbol(node.name, node.typeAnnotation));
       return null;
	}
// Hint: Functions must be registered in the current scope before visiting their body.
// 1. Ensure no variable with the same name exists in the current scope.
// 2. Collect the types of all parameters.
// 3. Construct the function type (parameter types → return type).
// 4. Add the function symbol to the current scope.
// 5. Enter the function’s scope and visit its contents.
   @Override
   public Void visitFunDecl(Absyn.FunDecl node) {
       // 1. Ensure no variable with the same name exists in the current scope.
       if (this.currentscope.hasLocalVar(node.name)) {
           throw new TypeCheckException("Tried to define fun ("+node.name+") but var with same name already exists");
       }
       // 2. Collect the types of all parameters.
       ArrayList<Type> paramTypes = new ArrayList<>();
       for (Decl d : node.params.list) {
         Absyn.Parameter p = (Absyn.Parameter) d;
         paramTypes.add(p.typeAnnotation);
       }
       // 3. Construct the function type (parameter types → return type).
       LIST paramList = new LIST(paramTypes);
       // 4. Add the function symbol to the current scope.
       this.currentscope.addFun(node.name, new FunSymbol(node.name, paramList, node.typeAnnotation));
       // 5. Enter the function's scope and visit its contents.
       this.currentscope = new Scope(this.currentscope);
       for (Decl d : node.params.list) {
         Absyn.Parameter p = (Absyn.Parameter) d;
         this.visitParameter(p);
}
       this.visit(node.body);
       this.currentscope = this.currentscope.getParent();
       return null;
   }
// Hint: Struct members are variables within the struct's scope.
// 1. Ensure no function with this name exists in the current scope.
// 2. Add the member as a variable symbol using its annotated type.
   @Override
   public Void visitStructMember(Absyn.StructMember node) {
       // 1. Ensure no function with this name exists in the current scope.
       if (this.currentscope.hasLocalFun(node.name)) {
           throw new TypeCheckException("Tried to define var ("+node.name+") but fun with same name already exists");
       }
       // 2. Add the member as a variable symbol using its annotated type.
       this.currentscope.addVar(node.name, new VarSymbol(node.name, node.typeAnnotation));
       return null;
   }
// Hint: Union members behave like variables within the union scope.
// 1. Ensure no function with this name exists in the current scope.
// 2. Add the member as a variable symbol using its annotated type.

    // Note: This is only true for now. Union's will get special treatement
    // later, but for now we treat them as the same as structs. 
   @Override
   public Void visitUnionMember(Absyn.UnionMember node) {
       // 1. Ensure no function with this name exists in the current scope.
       if (this.currentscope.hasLocalFun(node.name)) {
           throw new TypeCheckException("Tried to define var ("+node.name+") but fun with same name already exists");
       }
       // 2. Add the member as a variable symbol using its annotated type.
       this.currentscope.addVar(node.name, new VarSymbol(node.name, node.typeAnnotation));
       return null;
   }
// Hint: Variable declarations introduce a new variable in the current scope.
// 1. Ensure no function with this name exists in the current scope.
// 2. Add the variable symbol using its annotated type.
   @Override
   public Void visitVarDecl(Absyn.VarDecl node) {
       // 1. Ensure no function with this name exists in the current scope.
       if (this.currentscope.hasLocalFun(node.name)) {
           throw new TypeCheckException("Tried to define var ("+node.name+") but fun with same name already exists");
       }
       // 2. Add the variable symbol using its annotated type.
       this.currentscope.addVar(node.name, new VarSymbol(node.name, node.typeAnnotation));
       return null;
   }

}
