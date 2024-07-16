file:///C:/Users/kongs/OneDrive/Documents/ICCS311/Functional-and-Parallel-Programming-Project/Parallel/src/main/scala/ParSolver.scala
### java.lang.AssertionError: assertion failed

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/Users/kongs/OneDrive/Documents/ICCS311/Functional-and-Parallel-Programming-Project/Parallel/src/main/scala/ParSolver.scala
text:
```scala
package parf

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

object ParSolver {
  def parSolve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {
    if (puzzle.board(0)(0) != 0) return Some(puzzle.board) // Puzzle already solved

    var possibleNumber: ArrayBuffer[Int] = ArrayBuffer[Int]()
    var initial_row = 0
    var initial_col = 0

    while (puzzle.board(initial_row)(initial_col) != 0) {
      if (initial_col == puzzle.size - 1) {
        initial_row += 1
        initial_col = 0
      } else {
        initial_col += 1
      }
    }

    for (num <- 1 to puzzle.size) {
      if (puzzle.isValid(initial_row, initial_col, num)) {
        possibleNumber += num
      }
    }

    val solutionPromise = Promise[Option[Array[Array[Int]]]]()

    possibleNumber.foreach { num =>
      val future = Future {
        puzzle.board(initial_row)(initial_col) = num
        if (backtrack(initial_row, initial_col)) Some(puzzle.board) else None
      }

      future.onComplete {
        case Success(result) =>
          result match {
            case Some(board) => solutionPromise.trySuccess(Some(board))
            case None =>
          }
        case Failure(_) =>
      }
    }

    Try(Await.result(solutionPromise.future, Duration.Inf)) match {
      case Success(result) => result
      case Failure(_) => None // No solution found or error occurred
    }
  }
  def backtrack(row: Int, col: Int): Boolean = {
            if (row == puzzle.size) true // Puzzle solved
            else {
                val (nextRow, nextCol) = if (col == puzzle.size - 1) (row + 1, 0) else (row, col + 1)

                if (puzzle.board(row)(col) != 0) backtrack(nextRow, nextCol) // Skip filled cells
                else {
                var found = false
                for (num <- 1 to puzzle.size if !found) {
                    if (puzzle.isValid(row, col, num)) {
                    puzzle.board(row)(col) = num
                    if (backtrack(nextRow, nextCol)) found = true
                    else puzzle.board(row)(col) = 0 // Backtrack
                    }
                }
                found
                }
            }
        }

}
```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
	dotty.tools.dotc.core.TypeOps$.dominators$1(TypeOps.scala:248)
	dotty.tools.dotc.core.TypeOps$.approximateOr$1(TypeOps.scala:382)
	dotty.tools.dotc.core.TypeOps$.orDominator(TypeOps.scala:395)
	dotty.tools.dotc.core.Types$OrType.join(Types.scala:3588)
	dotty.tools.dotc.core.Types$Type.classSymbol(Types.scala:580)
	dotty.tools.dotc.typer.Applications.targetClass$1(Applications.scala:2331)
	dotty.tools.dotc.typer.Applications.harmonizeWith(Applications.scala:2338)
	dotty.tools.dotc.typer.Applications.harmonize(Applications.scala:2365)
	dotty.tools.dotc.typer.Applications.harmonize$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.harmonize(Typer.scala:120)
	dotty.tools.dotc.typer.Typer.$anonfun$36(Typer.scala:1921)
	dotty.tools.dotc.typer.Applications.harmonic(Applications.scala:2388)
	dotty.tools.dotc.typer.Applications.harmonic$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.harmonic(Typer.scala:120)
	dotty.tools.dotc.typer.Typer.typedMatchFinish(Typer.scala:1921)
	dotty.tools.dotc.typer.Typer.typedMatch(Typer.scala:1850)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3152)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.Namer.typedAheadExpr$$anonfun$1(Namer.scala:1690)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1680)
	dotty.tools.dotc.typer.Namer.typedAheadExpr(Namer.scala:1690)
	dotty.tools.dotc.typer.Namer.valOrDefDefSig(Namer.scala:1752)
	dotty.tools.dotc.typer.Namer.defDefSig(Namer.scala:1833)
	dotty.tools.dotc.typer.Namer$Completer.typeSig(Namer.scala:808)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:955)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:831)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:178)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:190)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:192)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:398)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:3084)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3109)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3220)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3324)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3370)
	dotty.tools.dotc.typer.Typer.typedBlockStats(Typer.scala:1209)
	dotty.tools.dotc.typer.Typer.typedBlock(Typer.scala:1213)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3145)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedFunctionValue(Typer.scala:1693)
	dotty.tools.dotc.typer.Typer.typedFunction(Typer.scala:1432)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3147)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedMatch(Typer.scala:1802)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3152)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.$anonfun$7(ProtoTypes.scala:496)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.cacheTypedArg(ProtoTypes.scala:419)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.typedArg(ProtoTypes.scala:497)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.typedArg(Applications.scala:913)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.typedArg(Applications.scala:913)
	dotty.tools.dotc.typer.Applications$Application.addTyped$1(Applications.scala:605)
	dotty.tools.dotc.typer.Applications$Application.matchArgs(Applications.scala:669)
	dotty.tools.dotc.typer.Applications$Application.init(Applications.scala:491)
	dotty.tools.dotc.typer.Applications$TypedApply.<init>(Applications.scala:795)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.<init>(Applications.scala:912)
	dotty.tools.dotc.typer.Applications.ApplyTo(Applications.scala:1142)
	dotty.tools.dotc.typer.Applications.ApplyTo$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.ApplyTo(Typer.scala:120)
	dotty.tools.dotc.typer.Applications.simpleApply$1(Applications.scala:985)
	dotty.tools.dotc.typer.Applications.realApply$1$$anonfun$2(Applications.scala:1068)
	dotty.tools.dotc.typer.Typer.tryEither(Typer.scala:3437)
	dotty.tools.dotc.typer.Applications.realApply$1(Applications.scala:1079)
	dotty.tools.dotc.typer.Applications.typedApply(Applications.scala:1117)
	dotty.tools.dotc.typer.Applications.typedApply$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.typedApply(Typer.scala:120)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3137)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.Typer.typedBlock(Typer.scala:1216)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3145)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.Namer.typedAheadExpr$$anonfun$1(Namer.scala:1690)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1680)
	dotty.tools.dotc.typer.Namer.typedAheadExpr(Namer.scala:1690)
	dotty.tools.dotc.typer.Namer.valOrDefDefSig(Namer.scala:1752)
	dotty.tools.dotc.typer.Namer.defDefSig(Namer.scala:1833)
	dotty.tools.dotc.typer.Namer$Completer.typeSig(Namer.scala:808)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:955)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:831)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:178)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:190)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:192)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:398)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:3084)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3109)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3220)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3324)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3370)
	dotty.tools.dotc.typer.Typer.typedBlockStats(Typer.scala:1209)
	dotty.tools.dotc.typer.Typer.typedBlock(Typer.scala:1213)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3145)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedFunctionValue(Typer.scala:1693)
	dotty.tools.dotc.typer.Typer.typedFunction(Typer.scala:1432)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3147)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.Typer.typedBlock(Typer.scala:1216)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3145)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.$anonfun$7(ProtoTypes.scala:496)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.cacheTypedArg(ProtoTypes.scala:419)
	dotty.tools.dotc.typer.ProtoTypes$FunProto.typedArg(ProtoTypes.scala:497)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.typedArg(Applications.scala:913)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.typedArg(Applications.scala:913)
	dotty.tools.dotc.typer.Applications$Application.addTyped$1(Applications.scala:605)
	dotty.tools.dotc.typer.Applications$Application.matchArgs(Applications.scala:669)
	dotty.tools.dotc.typer.Applications$Application.init(Applications.scala:491)
	dotty.tools.dotc.typer.Applications$TypedApply.<init>(Applications.scala:795)
	dotty.tools.dotc.typer.Applications$ApplyToUntyped.<init>(Applications.scala:912)
	dotty.tools.dotc.typer.Applications.ApplyTo(Applications.scala:1142)
	dotty.tools.dotc.typer.Applications.ApplyTo$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.ApplyTo(Typer.scala:120)
	dotty.tools.dotc.typer.Applications.simpleApply$1(Applications.scala:985)
	dotty.tools.dotc.typer.Applications.realApply$1$$anonfun$2(Applications.scala:1068)
	dotty.tools.dotc.typer.Typer.tryEither(Typer.scala:3437)
	dotty.tools.dotc.typer.Applications.realApply$1(Applications.scala:1079)
	dotty.tools.dotc.typer.Applications.typedApply(Applications.scala:1117)
	dotty.tools.dotc.typer.Applications.typedApply$(Applications.scala:351)
	dotty.tools.dotc.typer.Typer.typedApply(Typer.scala:120)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3137)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3351)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3370)
	dotty.tools.dotc.typer.Typer.typedBlockStats(Typer.scala:1209)
	dotty.tools.dotc.typer.Typer.typedBlock(Typer.scala:1213)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3145)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.Typer.$anonfun$63(Typer.scala:2627)
	dotty.tools.dotc.inlines.PrepareInlineable$.dropInlineIfError(PrepareInlineable.scala:256)
	dotty.tools.dotc.typer.Typer.typedDefDef(Typer.scala:2627)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3119)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3220)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3324)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3370)
	dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:2814)
	dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$1(Typer.scala:3125)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3129)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3220)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3324)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3370)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2947)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3171)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3221)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3298)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3302)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3413)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:47)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:477)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:53)
	dotty.tools.dotc.typer.TyperPhase.$anonfun$4(TyperPhase.scala:99)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:583)
	scala.collection.immutable.List.prependedAll(List.scala:152)
	scala.collection.immutable.List$.from(List.scala:684)
	scala.collection.immutable.List$.from(List.scala:681)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:898)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:100)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:315)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:337)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:350)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:360)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:360)
	dotty.tools.dotc.Run.compileSources(Run.scala:261)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:161)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:47)
	dotty.tools.pc.PcCollector.<init>(PcCollector.scala:42)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:109)
```
#### Short summary: 

java.lang.AssertionError: assertion failed