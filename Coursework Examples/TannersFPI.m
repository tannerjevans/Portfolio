% function [y, its] = TannersFPI(A, r, [optional arguments])
% 
% Computes the r'th root of A using a mixed Bisection 
% and Floating Point Iteration algorithm operating over
% a weighted Babylonian Map. Currently this function
% does not support custom starting points, and uses
% a multiplicative algorithm to locate and narrow the
% region of the root until FPI will progress faster
% (very, very soon for low r).
%
% The function returns just the solution (y) if called with
% no variable or a single variable assignment:
%    >> s = TannersFPI(A,r)
%    s =
%         [some ans]
%    >> TannersFPI(A,r)
%    ans =
%         [some ans]
% It can return the number of iterations required to reach
% the solution if called with an assignment of its return
% values to a two-valued array:
%    >> [s, i] = TannersFPI(A,r)
%    s =
%         [some ans]
%    i =
%         [some other ans]
%
% The optional arguments are entered as an ordered pair
% of the specifier followed by the preferred value:
%
%     Tolerance:
%       Specifies the level of precision to progress to
%       (the minimum difference between consecutive
%       iterations). By default, the tolerance is set to
%       eps(x), where x is some relevant value close
%       to where the algorithm is currently running. This
%       will be multiplied by whatever number you supply.
%         Specifier:         'tol'
%         Acceptable Values: (-inf, inf)
%         Default:           eps(x)
%
%     Graphing:
%       Specifies whether or not to show a stepwise
%       graph of the algorithm as it runs.
%         Specifier:         'g'
%         Acceptable Values: (-inf, inf) (treated as boolean)
%         Default:           0

function [y, varargout] = TannersFPI(A, r, varargin)

% Test switching from bisection to line-intercept 
    close all
    tol = 1;
    g = 1;
    
    for n = 1:2:size(varargin)
        if strcmp('tol', varargin{n})
            tol = abs(varargin{n+1});
        elseif strcmp('g', varargin{n})
            g = varargin{n+1};
        end
    end

    if ((A == 1) || (r == inf))
        y = 1;
        varargout = {0};
        return;
    end
    format long;
    w = (r-1)./r;
    winv = (1./r);
    f=@(x) w.*x + (winv.*A)./(x.^(r-1));
    iter = 0;
    dimStart = 0;
    dimEnd = 2*A^(1/r);
    frames = 8;
    activeLW = 1.5;
    inactiveLW = 0.3;
    if (g)
        ap = sprintf("%d", A);
        rp = sprintf("%d", r);
        bm = "Weighted Babylonian Map for x^{" + rp + "} = " + ap;
        display = fplot(f, "b-", 'DisplayName', bm, 'LineWidth', 1);
        set(gcf, 'Units', 'Pixels', 'OuterPosition',...
            [40, 40, 1000, 1000]);
        title('Stepwise Graph of Algorithm Progression');
        xlabel('x');
        ylabel('y');
        axis equal
        xlim([dimStart, dimEnd])
        ylim([dimStart, dimEnd])
        hold on
        l=@(x) x;
        fplot(l, "black", 'DisplayName', 'y = x', 'LineWidth', .4)
        legend('Location', 'southoutside');
    end
    left = 0;
    right = 1;
    fright = f(right);
    iter = iter + 1;
    if (g && ishghandle(display))
        it = 'Iteration';
        q = [right right];
        r = [0 fright];
        pit = sprintf('%d', iter);
        px = ': x = ';
        cx = sprintf('%-.23g', right);
        disp = it + " " + pit + px + cx;
        prevLine = plot(q, r, "r-", 'DisplayName', disp,...
            'LineWidth', activeLW);
        try
            waitforbuttonpress
        catch
        end
    end
    
    % Doubling algorithm to find the range that contains the root quickly
    while ((fright - right) > 0)
        left = right;
        right = right*1.5;
        if (iter == 1) right = left + 0.5; end
        fright = f(right);
        iter = iter + 1;
        if (g && ishghandle(display))
            q = [right right];
            if (fright > right) 
                r = [0 fright];
            else
                r = [0 right];
            end
            prevLine.Color = "k";
            prevLine.LineWidth = inactiveLW;
            prevLine.HandleVisibility= 'off';
            pit = sprintf('%d', iter);
            cx = sprintf('%-.23g', right);
            disp = it + " " + pit + px + cx;
            prevLine = plot(q, r, "r-", 'DisplayName', disp,...
                'LineWidth', activeLW);
            if (right > dimEnd*0.8)
                oldDimStart = dimStart;
                oldDimEnd = dimEnd;
                dimStart = 0;
                dimEnd = right*1.2;
                st = linspace(oldDimStart, dimStart, frames);
                en = linspace(oldDimEnd, dimEnd, frames);
                for t=1:frames
                    xlim([st(t), en(t)])
                    ylim([st(t), en(t)])
                    pause(1/1000);
                end
            end
            try
                waitforbuttonpress
            catch
            end
        end
    end
    if ((fright - right) == 0)
        y = right;
        varargout{1} = iter;
        return;
    end
    
    oldMid = 0;
    middle = (right + left)/2;
    fmiddle = f(middle);
    iter = iter + 1;
    
    if (g && ishghandle(display))
        q = [middle middle];
        if (fmiddle > middle) 
            r = [0 fmiddle];
        else
            r = [0 middle];
        end
        prevLine.Color = "k";
        prevLine.LineWidth = inactiveLW;
        prevLine.HandleVisibility = 'off';
        pit = sprintf('%d', iter);
        cx = sprintf('%-.23g', middle);
        disp = it + " " + pit + px + cx;
        prevLine = plot(q, r, "r-", 'DisplayName', disp,...
            'LineWidth', activeLW);
        width = right-left;
        oldDimStart = dimStart;
        oldDimEnd = dimEnd;
        dimStart = left-width*0.1;
        dimEnd = right+width*0.1;
        st = linspace(oldDimStart, dimStart, frames);
        en = linspace(oldDimEnd, dimEnd, frames);
        for t=1:frames
            xlim([st(t), en(t)])
            ylim([st(t), en(t)])
            pause(1/1000);
        end
        try
            waitforbuttonpress
        catch
        end
    end
    
    prevIter = iter;
    breakPoint = tol*eps(right-left);
    difff = right-middle;
    
    % Bisection to narrow range quickly
    while (difff > breakPoint && oldMid ~= fmiddle)
        % Floating Point Iteration to take over when bisection brings us
        % sufficiently close
        if (difff/2 <= (right - right*w))
            if (g && ishghandle(display))
                prevLine.Color = "k";
                prevLine.LineWidth = inactiveLW;
                prevLine.HandleVisibility = 'off';
                q = [middle fmiddle];
                r = [fmiddle fmiddle];
                pit = sprintf('%d', iter);
                cx = sprintf('%-.23g', fmiddle);
                disp = it + " " + pit + px + cx;
                prevLine = plot(q, r, "r-", 'DisplayName', disp,...
                    'LineWidth', activeLW);
                width = abs(fmiddle-middle);
                oldDimStart = dimStart;
                oldDimEnd = dimEnd;
                if (middle < fmiddle)
                    dimStart = middle-width*0.2;
                    dimEnd = fmiddle+width*0.2;
                else
                    dimStart = fmiddle-width*0.2;
                    dimEnd = middle+width*0.2;
                end
                st = linspace(oldDimStart, dimStart, frames);
                en = linspace(oldDimEnd, dimEnd, frames);
                for t=1:frames
                    xlim([st(t), en(t)])
                    ylim([st(t), en(t)])
                    pause(1/1000);
                end
                try
                    waitforbuttonpress
                catch
                end
            end
            oldMid = middle;
            middle = fmiddle;
            fmiddle = f(middle);
            difff = abs(middle - fmiddle);
            iter = iter + 1;
            prevIter = iter;
            % FPI until difference is less than a breakpoint, or until
            % the first time x begins to cycle between + and -
            while (difff > breakPoint && oldMid ~= fmiddle)
                if (g && ishghandle(display))
                    q = [middle middle];
                    r = [middle fmiddle];
                    prevLine.Color = "k";
                    prevLine.LineWidth = inactiveLW;
                    prevLine.HandleVisibility = 'off';
                    pit = sprintf('%d', iter);
                    cx = sprintf('%-.23g', fmiddle);
                    disp = it + " " + pit + px + cx;
                    prevLine = plot(q, r, "r-", 'DisplayName', disp,...
                        'LineWidth', activeLW);
                    oldDimStart = dimStart;
                    oldDimEnd = dimEnd;
                    if (middle < fmiddle)
                        dimStart = middle-difff*0.2;
                        dimEnd = fmiddle+difff*0.2;
                    else
                        dimStart = fmiddle-difff*0.2;
                        dimEnd = middle+difff*0.2;
                    end
                    st = linspace(oldDimStart, dimStart, frames);
                    en = linspace(oldDimEnd, dimEnd, frames);
                    for t=1:frames
                        xlim([st(t), en(t)])
                        ylim([st(t), en(t)])
                        pause(1/1000);
                    end
                    try
                        waitforbuttonpress
                    catch
                    end
                end
                if (g && ishghandle(display))
                    q = [middle fmiddle];
                    r = [fmiddle fmiddle];
                    prevLine.Color = "k";
                    prevLine.LineWidth = inactiveLW;
                    prevLine.HandleVisibility = 'off';
                    prevLine = plot(q, r, "r-", 'DisplayName', disp,...
                        'LineWidth', activeLW);
                    try
                        waitforbuttonpress
                    catch
                    end
                end
                oldMid = middle;
                middle = fmiddle;
                fmiddle = f(middle);
                difff = abs(middle - fmiddle);
                iter = iter + 1;
                breakPoint = ...
                    ((2*(iter-prevIter)/prevIter)^4)*tol*eps(right-left);
            end
            if (g && ishghandle(display))
                q = [middle middle];
                r = [middle fmiddle];
                prevLine.Color = "k";
                prevLine.LineWidth = inactiveLW;
                prevLine.HandleVisibility = 'off';
                pit = sprintf('%d', iter);
                cx = sprintf('%-.23g', fmiddle);
                disp = it + " " + pit + px + cx;
                plot(q, r, "r-", 'DisplayName', disp, 'LineWidth', activeLW);
            end
            y = (fmiddle+middle)/2;
            varargout{1} = iter;
            return;
        end
        
        % Bisection processes
        if ((fmiddle - middle) < 0)
            right = middle;
        elseif ((fmiddle - middle) > 0)
            left = middle;
        elseif ((fmiddle - middle) == 0)
            y = right;
            varargout{1} = iter;
            return;
        end
        oldMid = middle;
        middle = (right + left)/2;
        fmiddle = f(middle);
        difff = right-middle;
        iter = iter + 1;
        if (g && ishghandle(display))
            q = [middle middle];
            if (fmiddle > middle) 
                r = [0 fmiddle];
            else
                r = [0 middle];
            end
            prevLine.Color = "k";
            prevLine.LineWidth = inactiveLW;
            prevLine.HandleVisibility = 'off';
            pit = sprintf('%d', iter);
            cx = sprintf('%-.23g', middle);
            disp = it + " " + pit + px + cx;
            prevLine = plot(q, r, "r-", 'DisplayName', disp,...
                'LineWidth', activeLW);
            width = right-left;
            oldDimStart = dimStart;
            oldDimEnd = dimEnd;
            dimStart = left-width*0.1;
            dimEnd = right+width*0.1;
            st = linspace(oldDimStart, dimStart, frames);
            en = linspace(oldDimEnd, dimEnd, frames);
            for t=1:frames
                xlim([st(t), en(t)])
                ylim([st(t), en(t)])
                pause(1/1000);
            end
            try
                waitforbuttonpress
            catch
            end
        end
        breakPoint = ((2*(iter-prevIter)/prevIter)^4)*tol*eps(right-left);
    end
    y = middle;
    varargout{1} = iter;
end