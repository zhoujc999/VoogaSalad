package phase;

import graph.SimpleEdge;
import groovy.api.BlockGraph;
import groovy.api.GroovyFactory;
import phase.api.GameEvent;
import phase.api.Phase;
import phase.api.Transition;

/**
 *  The implementation of Transition initializes
 *  guard -> createGraph guard that passes everything in
 *  exec -> createGraph graph with nothing to execute
 */
public class TransitionImpl extends SimpleEdge<Phase> implements Transition {
    private GameEvent trigger;
    private BlockGraph guard;
    private BlockGraph execution;

    public TransitionImpl(Phase from, GameEvent trigger, Phase to) {
        super(from, to);
        this.trigger = trigger;
        this.guard = GroovyFactory.createGuard();
        this.execution = GroovyFactory.createGraph();
    }

    @Override
    public GameEvent trigger() { return trigger; }
    @Override
    public BlockGraph guard() { return guard; }
    @Override
    public BlockGraph exec() { return execution; }
}