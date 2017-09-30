# NormsABM
Simulations of social norms emergence and its sensitivity to social network structure

## The Model

he model consists of agents connected via links in a network, an adaptive mechanism which is used to re-seed the network with agents’ new behavioral information at evenly spaced time intervals based on the attributes of the selected agents, and a mutation mechanism. The model includes a parameter W – the mean probability of an agent witnessing a defection. An agent is then assigned its own probability of witnessing a defection from the normal distribution centered at W. This is the agent’s vigilance.

Inspired by Axelrod’s norms game each agent possesses a boldness value and a vengefulness value . Moreover every agent is assigned a score. However, because the motivation for this model is to model adaptation of behaviors in a network of actors rather than to simulate the evolution of agents, the payoffs are not reset at the beginning of each period.

## Agent Decision-Making

An agent will defect if its boldness is greater than the probability of being seen. The probability of being seen is directly tied to the number of an agent’s neighbors and their witnessing probabilities.

The second decision an agent has to make, is whether to punish a defection. If an agent’s neighbor defects, the model first checks whether the agent sees the defection which happens with probability equal to its vigilance. If the agent indeed sees the defection it then punishes the neighbor with probability equal to its vengefulness value.

After every four rounds each agent is evaluated together with all of its neighbors. The neighborhood is ranked by their payoffs. If the agent itself falls at least one standard deviation above the neighborhood’s mean payoff then the agent retains its current behavior. If it does not, then it randomly chooses any neighbor which lies at least one standard deviation above the neighborhood mean and copies that agent’s behavior for its own. An agent’s behavior also has a small probability of being randomly modified.
