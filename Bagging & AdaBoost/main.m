
%% hw3 for CS534- Bagging n adaBoost
% load data
load SPECT.mat;

dataTrain = train;
dataTest  = test;
% entry point
nEnsemble = [5, 10, 15, 20, 25, 30];

% error for bagging n adaboost
errTrain = zeros(6,2);
errTest = zeros(6,2);

% for each model 
for n = 1 : size(nEnsemble,2)
    fprintf('%dth modeling\n',n);
	[errTrain(n,1), errTest(n,1)] = funcBagging(dataTrain, dataTest, nEnsemble(n));

    [errTrain(n,2), errTest(n,2)] = funcAdaBoost(train, test, nEnsemble(n));    

end

% plot error
plot(nEnsemble,[errTrain, errTest]);
title ('test error');
xlabel('iteration');
ylabel('estimated error');
legend ('train error-Bagging', 'train error-AdaBoost','test error-Bagging', 'test error-AdaBoost');
print -djpeg 'result.jpg'

